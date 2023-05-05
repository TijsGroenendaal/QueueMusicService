package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.queuemusicfacade.services.UserLinkService
import nl.tijsgroenendaal.qumu.exceptions.AccessTokenExpiredException

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class UserLinkFacade(
    private val spotifyTokenService: SpotifyTokenClientService,
    private val spotifyLinkService: UserLinkService
) {

    fun getAccessToken(userId: UUID): String {
        return try {
            spotifyLinkService.getAccessToken(userId)
        } catch (e: AccessTokenExpiredException) {
            val refreshToken = spotifyLinkService.getRefreshToken(userId)

            val refreshedToken = spotifyTokenService.getRefreshedAccessToken(refreshToken)
            spotifyLinkService.updateLink(userId, refreshedToken)
            refreshedToken.accessToken
        }
    }

}