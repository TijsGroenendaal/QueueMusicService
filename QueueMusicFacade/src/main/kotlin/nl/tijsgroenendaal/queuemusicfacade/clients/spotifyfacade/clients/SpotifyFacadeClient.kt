package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.commands.reponses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetUserLinkByUserIdQueryResponse
import nl.tijsgroenendaal.qumusecurity.feign.QuMuFeignConfiguration

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

import java.util.UUID

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "spotify-facade-client",
    configuration = [QuMuFeignConfiguration::class]
)
interface SpotifyFacadeClient {

    @PostMapping("/v1/auth/logout")
    fun logout()

    @GetMapping("/v1/user-link/user/{userId}")
    fun getByUserId(@PathVariable userId: UUID): GetUserLinkByUserIdQueryResponse

    @GetMapping("/v1/spotify/tracks/{trackId}")
    fun getTrack(@PathVariable trackId: String): GetTrackQueryResponse

    @PostMapping("/v1/spotify/playlists")
    fun createPlaylist(@RequestParam("name") name: String): CreatePlaylistCommandResponse
}