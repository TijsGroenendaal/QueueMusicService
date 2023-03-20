package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.commands.AddSpotifySongCommand
import nl.tijsgroenendaal.spotifyfacade.facades.SpotifyFacade

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

import java.util.UUID

@RestController
class SpotifyFacadeController(
    private val spotifyFacade: SpotifyFacade
) {
    @PostMapping("/sessions/{sessionId}/songs/{songId}")
    fun addSong(@PathVariable sessionId: UUID, @PathVariable songId: String, @RequestBody command: AddSpotifySongCommand) {
        spotifyFacade.addPlaylistSong(sessionId, songId, command)
    }
}