package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.commands.AddPlaylistTrackCommand
import nl.tijsgroenendaal.spotifyfacade.commands.AddPlaylistTrackControllerCommand
import nl.tijsgroenendaal.spotifyfacade.facades.SpotifyApiFacade

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.RequestBody

import java.util.UUID

@RestController
@RequestMapping("/v1/spotify")
class SpotifyController(
    private val spotifyFacade: SpotifyApiFacade
) {

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @GetMapping("/tracks/{trackId}")
    fun getTrack(@PathVariable trackId: String): GetTrackQueryResponse {
        return spotifyFacade.getTrack(trackId)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PostMapping("/playlists")
    fun createPlaylist(@RequestParam("name") name: String, @RequestParam("user") userId: UUID): CreatePlaylistCommandResponse {
        return spotifyFacade.createPlaylist(name, userId)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PostMapping("/playlists/{playlistId}/tracks")
    fun addTrack(@PathVariable playlistId: String, @RequestBody command: AddPlaylistTrackControllerCommand) {
        spotifyFacade.addPlaylistTrack(
            AddPlaylistTrackCommand(
                command.userId,
                command.trackId,
                command.position,
                playlistId
            )
        )
    }

}