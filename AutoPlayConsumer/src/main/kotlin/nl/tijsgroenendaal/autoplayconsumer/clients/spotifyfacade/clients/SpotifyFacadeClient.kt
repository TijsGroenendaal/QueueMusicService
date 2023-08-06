package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.clients

import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands.QueueTrackCommand
import nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.configuration.SpotifyFacadeConfiguration

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${clients.spotify-facade}",
    name = "spotify-facade-client",
    configuration = [SpotifyFacadeConfiguration::class]
)
interface SpotifyFacadeClient {

    @PostMapping("/v1/spotify/player/queue")
    fun queueTrack(@RequestBody command: QueueTrackCommand)
}