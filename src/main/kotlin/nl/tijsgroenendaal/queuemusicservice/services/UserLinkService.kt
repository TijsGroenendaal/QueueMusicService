package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenModel
import nl.tijsgroenendaal.queuemusicservice.exceptions.AccessTokenExpiredException
import nl.tijsgroenendaal.queuemusicservice.exceptions.UnAuthorizedException
import nl.tijsgroenendaal.queuemusicservice.repositories.UserLinkRepository

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserLinkService(
    private val userLinkRepository: UserLinkRepository
) {

    @Throws(AccessTokenExpiredException::class)
    fun getAccessToken(userId: UUID): String {
        val userLink = userLinkRepository.findByUserModelId(userId)

        if (userLink?.linkAccessToken == null || userLink.linkExpireTime.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw AccessTokenExpiredException()
        }

        // Smart cast not possible due to mutability of attribute
        return userLink.linkAccessToken!!
    }

    fun getRefreshToken(userId: UUID): String {
        val userLink = userLinkRepository.findByUserModelId(userId)

        return userLink?.linkRefreshToken ?: throw UnAuthorizedException()
    }

    fun updateLink(userId: UUID, refreshedToken: RefreshedAccessTokenModel) {
        val userLink = userLinkRepository.findByUserModelId(userId) ?: throw UnAuthorizedException()

        userLink.apply {
            this.linkAccessToken = refreshedToken.accessToken
            this.linkExpireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds((refreshedToken.expiresIn).toLong())
        }

        userLinkRepository.save(userLink)
    }
}