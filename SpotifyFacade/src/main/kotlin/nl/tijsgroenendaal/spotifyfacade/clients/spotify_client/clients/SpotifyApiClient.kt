package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    name = "spotify-api-client",
    url = "\${clients.spotify-api}",
)
interface SpotifyApiClient {

    @GetMapping("/v1/me")
    fun getMe(@RequestHeader("Authorization") token: String): GetMeQueryResponse
    @PostMapping("/v1/me/player/queue")
    fun queueTrack(@RequestParam("uri") trackId: String, @RequestHeader("Authorization") token: String)
    @GetMapping("/v1/me")
    fun getSpotifyUserById(@RequestHeader("Authorization") token: String)

}