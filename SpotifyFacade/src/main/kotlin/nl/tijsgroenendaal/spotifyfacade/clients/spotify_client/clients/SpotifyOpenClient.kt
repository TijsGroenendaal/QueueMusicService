package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration.SpotifyOpenClientConfiguration
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable

@FeignClient(
    url = "\${clients.spotify-api}",
    name = "spotify-open-client",
    configuration = [SpotifyOpenClientConfiguration::class]
)
interface SpotifyOpenClient {

    @GetMapping("/v1/tracks/{trackId}")
    fun getTrack(@PathVariable trackId: String): GetTrackQueryResponse

}