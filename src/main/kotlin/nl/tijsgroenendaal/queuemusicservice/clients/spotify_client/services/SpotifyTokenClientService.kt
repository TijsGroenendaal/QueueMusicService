package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.RefreshTokenRequest
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenRequest(refreshToken))

}