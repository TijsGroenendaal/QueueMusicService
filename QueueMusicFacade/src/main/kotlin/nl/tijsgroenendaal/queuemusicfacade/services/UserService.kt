package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.entity.UserLinkModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.UserRepository
import nl.tijsgroenendaal.qumusecurity.security.model.Authorities
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicUserDetails
import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.UserErrorCodes
import nl.tijsgroenendaal.qumusecurity.security.helper.getUserIdFromSubject

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

import jakarta.transaction.Transactional

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

        return QueueMusicUserDetails(user.id, user.userDeviceLink?.deviceId, authorities.toSet())
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
                it ?: UserModel()
            }.apply {
                this.userLink = UserLinkModel(
                    this,
                    linkUser.id,
                    accessToken.refreshToken,
                    accessToken.accessToken,
                    LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessToken.expiresIn)
                )
            }

        return userRepository.save(persistentUser)
    }

    fun createAnonymousUser(deviceId: String): UserModel {
        val persistentUser = userRepository
            .findByUserDeviceLinkDeviceId(deviceId)
            .let {
                it ?: UserModel().apply {
                    this.userDeviceLink = UserDeviceLinkModel.new(deviceId, this)
                }
            }

        return userRepository.save(persistentUser)
    }

    fun logout(userId: UUID) {
        val user = findById(userId).apply {
            this.userRefreshToken = null
        }

        save(user)
    }

    @Transactional
    fun save(userModel: UserModel): UserModel = userRepository.save(userModel)
}