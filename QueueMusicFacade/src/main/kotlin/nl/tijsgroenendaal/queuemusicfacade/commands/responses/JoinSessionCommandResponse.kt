package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import java.time.LocalDateTime
import java.util.UUID

data class JoinSessionCommandResponse(
    val user: JoinSessionCommandResponseUser,
    val session: JoinSessionCommandResponseSession
)

data class JoinSessionCommandResponseSession(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String
)

data class JoinSessionCommandResponseUser(
    val id: UUID,
)