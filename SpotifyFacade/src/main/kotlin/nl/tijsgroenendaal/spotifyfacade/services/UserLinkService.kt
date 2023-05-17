package nl.tijsgroenendaal.spotifyfacade.services

import nl.tijsgroenendaal.qumu.exceptions.*
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
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
}