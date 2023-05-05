package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyApiClientService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/spotify")
class SpotifyTestController(
    private val spotifyApiClientService: SpotifyApiClientService
) {

    @GetMapping("/me")
    fun getMe(): Any {
        return spotifyApiClientService.getMe()
    }

    @GetMapping("/me/playlists")
    fun getMyPlaylists(): Any {
        return spotifyApiClientService.getMyPlaylists()
    }

}