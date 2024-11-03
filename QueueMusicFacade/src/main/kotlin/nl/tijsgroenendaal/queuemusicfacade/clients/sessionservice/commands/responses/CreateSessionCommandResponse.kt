package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses

import java.time.Instant
import java.util.UUID

data class CreateSessionCommandResponse(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val duration: Long,
    val code: String,
    val manualEnded: Boolean
)