package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyApiClientConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.users.GetMeQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient(
    url = "\${clients.spotify-api}",
    name = "spotify-api-client",
    configuration = [SpotifyApiClientConfiguration::class]
)
interface SpotifyApiClient {

    @GetMapping("/v1/me")
    fun getMe(): GetMeQueryResponse
    @GetMapping("/v1/me/playlists")
    fun getMyPlaylists(): Any

}