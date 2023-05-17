package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients

import nl.tijsgroenendaal.qumusecurity.feign.QuMuFeignConfiguration
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "spotify-facade-client",
    configuration = [QuMuFeignConfiguration::class]
)
interface SpotifyFacadeClient {

    @PostMapping("/v1/user-link/logout")
    fun logout()

}