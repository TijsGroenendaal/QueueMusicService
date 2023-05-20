package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.spotifyfacade.facades.SpotifyApiFacade

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/spotify")
class SpotifyController(
    private val spotifyApiClientService: SpotifyApiClientService,
    private val spotifyFacade: SpotifyApiFacade
) {

    @GetMapping("/tracks/{trackId}")
    fun getTrack(@PathVariable trackId: String): GetTrackQueryResponse {
        return spotifyApiClientService.getTrack(trackId)
    }

    @PostMapping("/playlists")
    fun createPlaylist(@RequestParam("name") name: String): CreatePlaylistCommandResponse {
        return spotifyFacade.createPlaylist(name)
    }

}