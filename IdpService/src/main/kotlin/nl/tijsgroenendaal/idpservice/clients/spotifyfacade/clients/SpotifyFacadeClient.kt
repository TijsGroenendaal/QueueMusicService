package nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients

import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.configuration.SpotifyFacadeConfiguration
import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.query.responses.GetUserLinkByUserIdQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

import java.util.UUID

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "spotify-facade-client",
    configuration = [SpotifyFacadeConfiguration::class]
)
interface SpotifyFacadeClient {

    @PostMapping("/v1/auth/logout")
    fun logout()

    @GetMapping("/v1/user-link/user/{userId}")
    fun getByUserId(@PathVariable userId: UUID): GetUserLinkByUserIdQueryResponse
}