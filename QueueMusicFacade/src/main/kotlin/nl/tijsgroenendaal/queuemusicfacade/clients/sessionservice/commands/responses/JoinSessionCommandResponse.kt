package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses

import java.time.Instant
import java.util.UUID

data class JoinSessionCommandResponse(
    val user: JoinSessionCommandResponseUser,
    val session: JoinSessionCommandResponseSession
)

data class JoinSessionCommandResponseSession(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String
)

data class JoinSessionCommandResponseUser(
    val id: UUID,
)