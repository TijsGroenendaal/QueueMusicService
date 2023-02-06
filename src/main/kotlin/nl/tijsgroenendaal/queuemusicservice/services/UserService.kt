package nl.tijsgroenendaal.queuemusicservice.services

import jakarta.transaction.Transactional
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.users.GetMeQueryResponse
import nl.tijsgroenendaal.queuemusicservice.exceptions.NotFoundException
import nl.tijsgroenendaal.queuemusicservice.models.UserLinkModel
import nl.tijsgroenendaal.queuemusicservice.models.UserModel
import nl.tijsgroenendaal.queuemusicservice.repositories.UserRepository

import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Throws(UsernameNotFoundException::class)
    fun findByUsername(username: UUID): UserModel {
        return userRepository
            .findById(username)
            .let {
                if (it.isEmpty) throw UsernameNotFoundException("User not found with id: $username")
                it.get()
            }
    }

    @Throws(NotFoundException::class)
    fun findById(id: UUID): UserModel {
        return userRepository
            .findById(id)
            .let {
                if(it.isEmpty) throw NotFoundException(id.toString())
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
                if (it == null) {
                    val user = UserModel().apply {
                        this.userLink = UserLinkModel(
                            this,
                            linkUser.id,
                            accessToken.refreshToken,
                            accessToken.accessToken,
                            LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessToken.expiresIn)
                        )
                    }
                    return@let user
                }
                return@let it
            }

        return userRepository.save(persistentUser)
    }

    @Transactional
    fun save(userModel: UserModel): UserModel = userRepository.save(userModel)
}