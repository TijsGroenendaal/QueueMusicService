package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import java.time.LocalDateTime
import java.util.UUID

data class AddSessionSongCommandResponse(
    val id: UUID,
    val user: AddSessionSongCommandResponseUser,
    val session: AddSessionSongCommandResponseSession,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val createdAt: LocalDateTime,
)

data class AddSessionSongCommandResponseUser(
    val id: UUID,
)

data class AddSessionSongCommandResponseSession(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String
)
