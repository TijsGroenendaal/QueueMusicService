package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyLoginClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse

import org.springframework.stereotype.Service

@Service
class SpotifyApiClientService(
    private val spotifyLoginClient: SpotifyLoginClient
) {

    /**
     * Use this when you have no AuthenticationContext configured
     */
    fun getMe(accessCode: String): GetMeQueryResponse {
        return spotifyLoginClient.getMeWithAccessToken("Bearer $accessCode")
    }

}