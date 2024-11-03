package nl.tijsgroenendaal.sessionservice.session

import java.util.UUID

data class CreateSessionRequest(
    val autoplayAcceptance: Int?,
    val code: String,
    val duration: Long,
    val userId: UUID,
    val maxUsers: Int
)
