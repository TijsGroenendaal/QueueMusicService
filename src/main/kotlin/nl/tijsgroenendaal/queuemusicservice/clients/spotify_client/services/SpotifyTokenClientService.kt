package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.AccessTokenQuery
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.RefreshTokenQuery
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getAccessToken(code: String): AccessTokenResponseModel {
        return spotifyTokenClient.getAccessToken(AccessTokenQuery(code).toForm())
    }

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenResponseModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenQuery(refreshToken).toForm())

}