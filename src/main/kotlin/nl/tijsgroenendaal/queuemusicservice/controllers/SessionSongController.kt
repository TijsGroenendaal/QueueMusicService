package nl.tijsgroenendaal.queuemusicservice.controllers

import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicservice.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.entity.SessionSong
import nl.tijsgroenendaal.queuemusicservice.facades.SessionSongFacade

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
    ): SessionSong {
        return sessionSongFacade.addSpotifySessionSong(command, sessionId)
    }

    @PostMapping()
    fun addSessionSong(
        @PathVariable sessionId: UUID,
        @RequestBody command: AddSessionSongControllerCommand
    ): SessionSong {
        return sessionSongFacade.addSessionSong(command, sessionId)
    }

}