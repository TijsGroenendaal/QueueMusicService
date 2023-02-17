package nl.tijsgroenendaal.queuemusicservice.commands

data class CreateSessionCommand(
    val duration: Long,
    val maxUsers: Int = 10
)
