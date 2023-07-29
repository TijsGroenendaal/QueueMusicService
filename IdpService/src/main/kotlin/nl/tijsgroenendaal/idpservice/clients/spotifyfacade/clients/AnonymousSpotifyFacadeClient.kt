package nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

import java.util.UUID

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "anonymous-spotify-facade-client",
)
interface AnonymousSpotifyFacadeClient {

    @PostMapping("/v1/auth/login")
    fun login(@RequestParam(name = "code") code: String): UUID

}