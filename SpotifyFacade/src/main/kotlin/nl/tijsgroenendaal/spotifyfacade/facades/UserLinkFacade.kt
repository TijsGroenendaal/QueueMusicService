package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class UserLinkFacade(
    private val spotifyClientService: SpotifyTokenClientService,
    private val userLinkService: UserLinkService
) {

    fun getAccessTokenBySessionId(sessionId: UUID): String {
        val userLink = userLinkService.getLinkBySessionId(sessionId)
        if (userLink.linkExpireTime.isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            return userLink.linkAccessToken
        }

        return refreshAccessToken(userLink.id, userLink.linkRefreshToken)
    }

    private fun refreshAccessToken(userLink: UUID, refreshToken: String): String {
        val refreshedAccessToken = spotifyClientService.getRefreshedAccessToken(refreshToken)
        userLinkService.updateAccessTokens(userLink, refreshedAccessToken);

        return refreshedAccessToken.accessToken
    }
}