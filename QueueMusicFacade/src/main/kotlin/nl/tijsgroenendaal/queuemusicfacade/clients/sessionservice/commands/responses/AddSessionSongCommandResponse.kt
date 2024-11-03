package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses

import java.time.Instant
import java.util.UUID

data class AddSessionSongCommandResponse(
    val id: UUID,
    val user: AddSessionSongCommandResponseUser,
    val session: AddSessionSongCommandResponseSession,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val createdAt: Instant,
)

data class AddSessionSongCommandResponseUser(
    val id: UUID,
)

data class AddSessionSongCommandResponseSession(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String
)
