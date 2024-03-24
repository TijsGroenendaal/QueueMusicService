package nl.tijsgroenendaal.sessionservice.services.commands

import java.util.UUID

data class CreateSessionCommand(
    val autoplayAcceptance: Int?,
    val code: String,
    val duration: Long,
    val userId: UUID,
    val maxUsers: Int
)
