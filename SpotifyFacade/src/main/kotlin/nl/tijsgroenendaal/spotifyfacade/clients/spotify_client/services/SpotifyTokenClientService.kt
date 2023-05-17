package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.AccessTokenQuery
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.AccessTokenResponseModel

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getAccessToken(code: String): AccessTokenResponseModel =
        spotifyTokenClient.getAccessToken(AccessTokenQuery(code).toForm())

}