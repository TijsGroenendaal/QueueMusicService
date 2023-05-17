package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration.SpotifyTokenClientConfiguration
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration.FormEncodedConfiguration
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.AccessTokenResponseModel

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    url = "\${clients.account-spotify-api}",
    name = "spotify-token-client",
    configuration = [SpotifyTokenClientConfiguration::class, FormEncodedConfiguration::class]
)
interface SpotifyTokenClient {

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getAccessToken(
        form: Map<String, Any>
    ): AccessTokenResponseModel

}