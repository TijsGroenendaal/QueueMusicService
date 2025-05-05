package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.query.responses

import java.time.Instant
import java.util.UUID

data class GetSessionResponse(
    val id: UUID,
    val host: UUID,
    val duration: Long,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String,
    val autoplayAcceptance: Int?,
    val maxUsers: Int,
    val manualEnded: Boolean,
    val sessionUsers: List<GetSessionResponseUser>
)

data class GetSessionResponseUser(
    val user: UUID,
)
