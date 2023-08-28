package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.controllers.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.controllers.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.facades.SessionSongFacade

import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.security.access.prepost.PreAuthorize

import java.util.UUID

import nl.tijsgroenendaal.queuemusicfacade.commands.VoteSessionSongCommand as VoteSessionSongFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand as AddSessionSongFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AcceptSessionSongCommand as AcceptSessionSongFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.DeleteSessionSongCommand as DeleteSessionSongFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand as AddSpotifySessionSongFacadeCommand

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
        return sessionSongFacade.addSpotifySessionSong(AddSpotifySessionSongFacadeCommand(
            command.songId,
            sessionId
        ))
    }

    @PostMapping
    fun addSessionSong(
        @RequestBody command: AddSessionSongCommand,
        @PathVariable sessionId: UUID
    ): AddSessionSongCommandResponse {
        return sessionSongFacade.addSessionSong(AddSessionSongFacadeCommand(
            null,
            command.album,
            command.name,
            command.artists,
            sessionId
        ))
    }

    @PutMapping("/{songId}")
    fun voteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam vote: String
    ): VoteSessionSongCommandResponse {
        return sessionSongFacade.voteSessionSong(VoteSessionSongFacadeCommand(
            sessionId,
            songId,
            vote
        ))
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @DeleteMapping("/{songId}")
    fun deleteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
    ): ResponseEntity<Any> {
        sessionSongFacade.deleteSessionSong(DeleteSessionSongFacadeCommand(
            songId,
            sessionId
        ))

        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{songId}/accept")
    fun acceptSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID
    ){
        sessionSongFacade.acceptSessionSong(AcceptSessionSongFacadeCommand(
            sessionId,
            songId
        ))
    }

}