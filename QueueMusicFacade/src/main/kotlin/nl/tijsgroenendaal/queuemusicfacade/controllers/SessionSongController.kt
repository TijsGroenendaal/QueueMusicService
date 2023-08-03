package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongUserVoteModel.Companion.toResponse
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.VoteEnum
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

import java.util.UUID
import org.springframework.security.access.prepost.PreAuthorize

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

    @PostMapping
    fun addSessionSong(
        @PathVariable sessionId: UUID,
        @RequestBody command: AddSessionSongControllerCommand
    ): AddSessionSongCommandResponse {
        return sessionSongFacade.addSessionSong(command, sessionId).toResponse()
    }

    @PutMapping("/{songId}")
    fun voteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam vote: VoteEnum
    ): VoteSessionSongCommandResponse {
        return sessionSongFacade.voteSessionSong(sessionId, songId, vote).toResponse()
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @DeleteMapping("/{songId}")
    fun deleteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
    ): ResponseEntity<Any> {
        sessionSongFacade.deleteSessionSong(sessionId, songId)

        return ResponseEntity.noContent().build()
    }

}