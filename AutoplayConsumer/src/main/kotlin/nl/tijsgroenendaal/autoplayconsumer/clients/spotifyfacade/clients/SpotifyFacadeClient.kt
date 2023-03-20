package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.clients

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    url = "\${clients.spotify-facade-api}",
    name = "spotify-facade-api-client"
)
interface SpotifyFacadeClient {
        @PostMapping("/sessions/{sessionId}/songs/{songId}")
    fun addSessionSong(@PathVariable sessionId: String, @PathVariable songId: String)
}