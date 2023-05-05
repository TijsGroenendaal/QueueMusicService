package nl.tijsgroenendaal.queuemusicfacade.services.commands

import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel

data class CreateSessionCommand(
    val playlistId: String,
    val code: String,
    val duration: Long,
    val userModel: UserModel,
    val maxUsers: Int
)
