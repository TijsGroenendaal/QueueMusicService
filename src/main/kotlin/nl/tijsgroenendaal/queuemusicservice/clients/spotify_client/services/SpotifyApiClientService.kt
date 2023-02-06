package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyApiClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyLoginClient
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.users.GetMeQueryResponse

import org.springframework.stereotype.Service

@Service
class SpotifyApiClientService(
    private val spotifyApiClient: SpotifyApiClient,
    private val spotifyLoginClient: SpotifyLoginClient
) {

    /**
     * Use this when you have no AuthenticationContext configured
     */
    fun getMe(accessCode: String): GetMeQueryResponse {
        return spotifyLoginClient.getMeWithAccessToken("Bearer $accessCode")
    }

    fun getMe(): GetMeQueryResponse {
        return spotifyApiClient.getMe()
    }

}