package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses

import java.time.LocalDateTime
import java.util.UUID

data class CreateSessionCommandResponse(
    val id: UUID,
    val playlistId: String?,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val duration: Long,
    val code: String,
    val manualEnded: Boolean
)