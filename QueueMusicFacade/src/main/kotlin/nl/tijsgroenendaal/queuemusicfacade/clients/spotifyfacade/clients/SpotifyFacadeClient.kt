package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.configuration.SpotifyFacadeConfiguration
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetTrackQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "spotify-facade-client",
    configuration = [SpotifyFacadeConfiguration::class]
)
interface SpotifyFacadeClient {

    @GetMapping("/v1/spotify/tracks/{trackId}")
    fun getTrack(@PathVariable trackId: String): GetTrackQueryResponse
}