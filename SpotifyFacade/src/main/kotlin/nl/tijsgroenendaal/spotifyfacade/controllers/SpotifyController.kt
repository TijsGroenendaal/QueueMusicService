package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse
import nl.tijsgroenendaal.spotifyfacade.commands.QueueTrackCommand
import nl.tijsgroenendaal.spotifyfacade.facades.SpotifyApiFacade

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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
    @PostMapping("/player/queue")
    fun queueTrack(@RequestBody command: QueueTrackCommand) {
        spotifyFacade.queueTrack(command)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @GetMapping("/user/{userId}")
    fun getSpotifyUserById(@PathVariable userId: UUID): GetMeQueryResponse {
        return spotifyFacade.getSpotifyUserById(userId)
    }

}