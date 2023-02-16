package nl.tijsgroenendaal.queuemusicservice.commands

data class AddSessionSongControllerCommand(
    val album: String,
    val authors: String,
    val name: String
)