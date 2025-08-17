package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.song.jpa.SongState
import java.time.Instant
import java.util.UUID

data class GetSongsRequestResponse(
    val id: UUID,
    val user: UUID,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val votes: Int,
    val createdAt: Instant,
    val state: SongState,
)