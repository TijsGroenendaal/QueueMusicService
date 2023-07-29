package nl.tijsgroenendaal.queuemusicfacade.services.commands

import java.util.UUID

data class CreateSessionCommand(
    val playlistId: String?,
    val code: String,
    val duration: Long,
    val userId: UUID,
    val maxUsers: Int
)
