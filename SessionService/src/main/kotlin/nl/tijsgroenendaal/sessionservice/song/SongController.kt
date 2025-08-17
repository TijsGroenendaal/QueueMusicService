package nl.tijsgroenendaal.sessionservice.song

import nl.tijsgroenendaal.sessionservice.requests.responses.AddSessionSongResponse
import nl.tijsgroenendaal.sessionservice.requests.responses.GetSongsRequestResponse
import nl.tijsgroenendaal.sessionservice.requests.responses.VoteSessionSongResponse
import nl.tijsgroenendaal.sessionservice.songvote.jpa.VoteEnum
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/v1/sessions/{sessionId}/songs")
class SongController(
    private val songFacade: SongFacade
) {

    @PostMapping
    fun addSong(
        @PathVariable sessionId: UUID,
        @RequestBody command: AddSongControllerRequest,
        @RequestParam userId: UUID
    ): AddSessionSongResponse {
        val result = songFacade.createSong(
            AddSongRequest(
                command.trackId,
                command.trackAlbum,
                command.trackName,
                command.trackArtists,
                sessionId
            ), userId
        )

        return AddSessionSongResponse(result)
    }

    @PutMapping("/{songId}")
    fun voteSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam vote: VoteEnum,
        @RequestParam userId: UUID
    ): VoteSessionSongResponse {
        val result = songFacade.voteSong(sessionId, songId, vote, userId)
        return VoteSessionSongResponse(result)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @DeleteMapping("/{songId}")
    fun deleteSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam userId: UUID
    ): ResponseEntity<Any> {
        songFacade.deleteSong(sessionId, songId, userId)

        return ResponseEntity.noContent().build()
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{songId}/accept")
    fun acceptSong(
        @PathVariable sessionId: UUID,
        @PathVariable songId: UUID,
        @RequestParam userId: UUID
    ) {
        songFacade.acceptSong(sessionId, songId, userId)
    }

    @GetMapping
    fun getSongs(
        @PathVariable sessionId: UUID,
        @RequestParam userId: UUID
    ): List<GetSongsRequestResponse> {
        return songFacade.getSongs(sessionId, userId)
    }

}