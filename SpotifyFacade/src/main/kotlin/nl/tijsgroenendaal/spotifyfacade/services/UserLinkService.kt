package nl.tijsgroenendaal.spotifyfacade.services

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel
import nl.tijsgroenendaal.spotifyfacade.exceptions.BadRequestException
import nl.tijsgroenendaal.spotifyfacade.repositories.UserLinkRepository
import nl.tijsgroenendaal.spotifyfacade.repositories.models.UserLinkModel

import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserLinkService(
    private val userLinkRepository: UserLinkRepository
) {

    fun getLinkBySessionId(sessionId: UUID): UserLinkModel {
        return userLinkRepository.findUserLinkBySessionHost(sessionId)
            ?: throw BadRequestException(HttpStatus.NOT_FOUND.value().toString(), "UserLink not found in session $sessionId")
    }

    fun updateAccessTokens(linkId: UUID, responseModel: RefreshedAccessTokenResponseModel) {
        val expireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(responseModel.expiresIn)

        userLinkRepository.updateAccessTokens(linkId, responseModel.accessToken, expireTime)
    }

}