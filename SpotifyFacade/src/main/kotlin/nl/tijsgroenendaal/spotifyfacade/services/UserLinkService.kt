package nl.tijsgroenendaal.spotifyfacade.services

import nl.tijsgroenendaal.qumu.exceptions.*
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel
import nl.tijsgroenendaal.spotifyfacade.entity.UserLinkModel
import nl.tijsgroenendaal.spotifyfacade.repositories.UserLinkRepository

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserLinkService(
    private val userLinkRepository: UserLinkRepository
) {

    fun findByUserId(userId: UUID): UserLinkModel {
        return userLinkRepository.findByUserModelId(userId)
            ?: throw BadRequestException(SessionErrorCodes.NO_USERLINK_FOUND, "User $userId has no UserLink defined")
    }

    fun logout() {
        try {
            val userLink = findByUserId(getAuthenticationContextSubject())

            userLink.linkAccessToken = null
            userLink.linkRefreshToken = null
            userLink.linkExpireTime = LocalDateTime.now(ZoneOffset.UTC)

            userLinkRepository.save(userLink)
        } catch (e: BadRequestException) {
            return
        }
    }

    fun findByLinkId(linkId: String): UserLinkModel {
        return userLinkRepository.findByLinkId(linkId) ?: throw BadRequestException(UserLinkErrorCodes.USER_LINK_NOT_FOUND, "UserLink $linkId not found")
    }

    fun create(accessToken: String, refreshToken: String, expiresIn: Long, id: String): UserLinkModel {
        val userLink = UserLinkModel(
            id,
            refreshToken,
            accessToken,
            LocalDateTime.now(ZoneOffset.UTC).plusSeconds(expiresIn)
        )

        return userLinkRepository.save(userLink)
    }

    @Throws(AccessTokenExpiredException::class)
    fun getAccessToken(userId: UUID): String {
        val userLink = findByUserId(userId)

        if (userLink.linkAccessToken == null || userLink.linkExpireTime.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw AccessTokenExpiredException()
        }

        // Smart cast not possible due to mutability of attribute
        return userLink.linkAccessToken!!
    }

    fun getRefreshToken(userId: UUID): String {
        val userLink = findByUserId(userId)

        return userLink.linkRefreshToken ?: throw RefreshTokenExpiredException()
    }

    fun update(userId: UUID, refreshedToken: RefreshedAccessTokenResponseModel) {
        val userLink = findByUserId(userId)

        userLink.apply {
            this.linkAccessToken = refreshedToken.accessToken
            this.linkExpireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(refreshedToken.expiresIn)
        }

        save(userLink)
    }

    fun update(userLink: UserLinkModel, accessToken: AccessTokenResponseModel) {
        userLink.apply {
            this.linkAccessToken = accessToken.accessToken
            this.linkRefreshToken = accessToken.refreshToken
            this.linkExpireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(accessToken.expiresIn)
        }

        save(userLink)
    }

    private fun save(userLink: UserLinkModel) = userLinkRepository.save(userLink)
}