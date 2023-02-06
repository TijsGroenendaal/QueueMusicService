package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.AccessTokenRequest
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.RefreshTokenRequest
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenResponseModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getAccessToken(code: String): AccessTokenResponseModel {
        return spotifyTokenClient.getAccessToken(AccessTokenRequest(code).toForm())
    }

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenResponseModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenRequest(refreshToken).toForm())

}