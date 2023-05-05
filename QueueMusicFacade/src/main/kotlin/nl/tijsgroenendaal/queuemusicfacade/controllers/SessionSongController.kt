package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.queuemusicfacade.facades.SessionSongFacade

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable

import java.util.UUID

@RestController
@RequestMapping("/v1/sessions/{sessionId}/songs")
class SessionSongController(
    private val sessionSongFacade: SessionSongFacade
) {

    @PostMapping("/spotify")
    fun addSpotifySessionSong(
        @RequestBody command: AddSpotifySessionSongCommand,
        @PathVariable sessionId: UUID
    ): AddSessionSongCommandResponse {
        return sessionSongFacade.addSpotifySessionSong(command, sessionId).toResponse()
    }

    @PostMapping()
    fun addSessionSong(
        @PathVariable sessionId: UUID,
        @RequestBody command: AddSessionSongControllerCommand
    ): AddSessionSongCommandResponse {
        return sessionSongFacade.addSessionSong(command, sessionId).toResponse()
    }

}