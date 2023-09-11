package nl.tijsgroenendaal.sessionservice.controllers

import nl.tijsgroenendaal.sessionservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.sessionservice.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.sessionservice.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.sessionservice.commands.responses.AddSessionSongCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.sessionservice.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.sessionservice.entity.SessionSongUserVoteModel.Companion.toResponse
import nl.tijsgroenendaal.sessionservice.entity.enums.VoteEnum
import nl.tijsgroenendaal.sessionservice.facades.SessionSongFacade

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

@RestController
@RequestMapping("/v1/sessions/{sessionId}/songs")
class SessionSongController(
    private val sessionSongFacade: SessionSongFacade
) {

    @PostMapping
    fun addSessionSong(
        @PathVariable sessionId: UUID,
        @RequestBody command: AddSessionSongControllerCommand,
        @RequestParam userId: UUID
    ): AddSessionSongCommandResponse {
        return sessionSongFacade.createSessionSong(AddSessionSongCommand(
            command.trackId,
            command.trackAlbum,
            command.trackName,
            command.trackArtists,
            sessionId
        ), userId).toResponse()
    }

    @PutMapping("/{songId}")
    fun voteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam vote: VoteEnum,
        @RequestParam userId: UUID
    ): VoteSessionSongCommandResponse {
        return sessionSongFacade.voteSessionSong(sessionId, songId, vote, userId).toResponse()
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @DeleteMapping("/{songId}")
    fun deleteSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam userId: UUID
    ): ResponseEntity<Any> {
        sessionSongFacade.deleteSessionSong(sessionId, songId, userId)

        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{songId}/accept")
    fun acceptSessionSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam userId: UUID
    ){
        sessionSongFacade.acceptSessionSong(sessionId, songId, userId)
    }

}