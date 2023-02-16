package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.users.GetMeQueryResponse
import nl.tijsgroenendaal.queuemusicservice.helper.getUserIdFromSubject
import nl.tijsgroenendaal.queuemusicservice.entity.UserLinkModel
import nl.tijsgroenendaal.queuemusicservice.entity.UserModel
import nl.tijsgroenendaal.queuemusicservice.repositories.UserRepository
import nl.tijsgroenendaal.queuemusicservice.security.model.Authorities
import nl.tijsgroenendaal.queuemusicservice.security.model.QueueMusicUserDetails

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

import jakarta.transaction.Transactional
import nl.tijsgroenendaal.queuemusicservice.exceptions.BadRequestException
import nl.tijsgroenendaal.queuemusicservice.exceptions.UserErrorCodes

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        return findUserDetailsById(getUserIdFromSubject(username))
    }

    fun findUserDetailsById(id: UUID): QueueMusicUserDetails {
        val user = findById(id)

        val authorities = mutableListOf(Authorities.REFRESH)
        if (user.userLink != null) {
            authorities.add(Authorities.SPOTIFY)
        }

        return QueueMusicUserDetails(user, authorities.toSet())
    }

    fun findById(id: UUID): UserModel {
        return userRepository
            .findById(id)
            .let {
                if(it.isEmpty) throw BadRequestException(UserErrorCodes.USER_NOT_FOUND, "User $id not found")
                it.get()
            }
    }

    @Transactional
    fun createUser(
        linkUser: GetMeQueryResponse,
        accessToken: AccessTokenResponseModel
    ): UserModel {
        val persistentUser = userRepository
            .findByUserLinkLinkId(linkUser.id)
            .let {
                it ?: UserModel().apply {
                    this.userLink = UserLinkModel(
                    this,
                        linkUser.id,
                        accessToken.refreshToken,
                        accessToken.accessToken,
                        LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessToken.expiresIn)
                    )
                }
            }

        return userRepository.save(persistentUser)
    }

    @Transactional
    fun save(userModel: UserModel): UserModel = userRepository.save(userModel)
}