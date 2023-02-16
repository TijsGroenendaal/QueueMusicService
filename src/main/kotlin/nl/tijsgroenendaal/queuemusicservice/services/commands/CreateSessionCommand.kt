package nl.tijsgroenendaal.queuemusicservice.services.commands

import nl.tijsgroenendaal.queuemusicservice.entity.UserModel

data class CreateSessionCommand(
    val playlistId: String,
    val code: String,
    val duration: Long,
    val userModel: UserModel
)
