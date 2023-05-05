package nl.tijsgroenendaal.queuemusicfacade.commands

data class AddSessionSongControllerCommand(
    val album: String,
    val authors: String,
    val name: String
)