package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.CredentialsTokenQuery
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.RefreshTokenQuery
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.auth.CredentialsTokenResponseModel
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenResponseModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenQuery(refreshToken).toForm())

    fun getCredentialsAccessToken(): CredentialsTokenResponseModel =
        spotifyTokenClient.getCredentialsToken(CredentialsTokenQuery().toForm())

}