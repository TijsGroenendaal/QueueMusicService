package nl.tijsgroenendaal.queuemusicfacade.commands

data class CreateSessionCommand(
    val duration: Long,
    val maxUsers: Int = 10,
    val autoplay: Boolean = false
)
