package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.track.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "spotify-login-client",
    url = "\${clients.spotify-api}"
)
interface SpotifyLoginClient {

    @GetMapping("/v1/me")
    fun getMeWithAccessToken(@RequestHeader(value = "Authorization", required = true) accessToken: String): GetMeQueryResponse

    @GetMapping("/v1/tracks/{songId}")
    fun getTrack(@RequestHeader(value = "Authorization", required = true) accessToken: String, @PathVariable songId: String): GetTrackQueryResponse

}