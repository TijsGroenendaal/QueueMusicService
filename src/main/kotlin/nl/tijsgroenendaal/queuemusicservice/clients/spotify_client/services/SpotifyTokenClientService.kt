package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.AccessTokenRequest
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.RefreshTokenRequest
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.AccessTokenModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getAccessToken(code: String): AccessTokenModel {
        return spotifyTokenClient.getAccessToken(AccessTokenRequest(code))
    }

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenRequest(refreshToken))

}