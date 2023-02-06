package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.users.GetMeQueryResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "spotify-login-client",
    url = "\${clients.spotify-api}"
)
interface SpotifyLoginClient {

    @GetMapping("/v1/me")
    fun getMeWithAccessToken(@RequestHeader(value = "Authorization", required = true) accessToken: String): GetMeQueryResponse

}