package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.qumu.exceptions.AccessTokenExpiredException
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.spotifyfacade.queries.responses.GetUserLinkByUserIdQueryResponse
import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class UserLinkFacade(
    private val spotifyTokenClientService: SpotifyTokenClientService,
    private val spotifyLinkService: UserLinkService
) {

    fun getAccessToken(userId: UUID): String {
        return try {
            spotifyLinkService.getAccessToken(userId)
        } catch (e: AccessTokenExpiredException) {
            val refreshToken = spotifyLinkService.getRefreshToken(userId)

            val refreshedToken = spotifyTokenClientService.getRefreshedAccessToken(refreshToken)
            spotifyLinkService.update(userId, refreshedToken)
            return refreshedToken.accessToken
        }
    }

    fun getByUserId(userId: UUID): GetUserLinkByUserIdQueryResponse {
        return spotifyLinkService.findByUserId(userId).let {
            GetUserLinkByUserIdQueryResponse(it.id, it.userModelId, it.linkId)
        }
    }
}