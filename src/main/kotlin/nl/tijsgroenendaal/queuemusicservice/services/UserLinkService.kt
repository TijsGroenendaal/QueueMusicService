package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.entity.UserLinkModel
import nl.tijsgroenendaal.queuemusicservice.exceptions.AccessTokenExpiredException
import nl.tijsgroenendaal.queuemusicservice.exceptions.BadRequestException
import nl.tijsgroenendaal.queuemusicservice.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.queuemusicservice.exceptions.UnAuthorizedException
import nl.tijsgroenendaal.queuemusicservice.repositories.UserLinkRepository

import jakarta.transaction.Transactional

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

    @Transactional
    fun updateLink(userId: UUID, refreshedToken: RefreshedAccessTokenResponseModel) {
        val userLink = userLinkRepository.findByUserModelId(userId) ?: throw UnAuthorizedException()

        userLink.apply {
            this.linkAccessToken = refreshedToken.accessToken
            this.linkExpireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds((refreshedToken.expiresIn))
        }

        userLinkRepository.save(userLink)
    }

    fun findByUserId(userId: UUID): UserLinkModel {
        return userLinkRepository.findByUserModelId(userId)
            ?: throw BadRequestException(SessionErrorCodes.NO_USERLINK_FOUND, "User $userId has no UserLink defined")
    }
}