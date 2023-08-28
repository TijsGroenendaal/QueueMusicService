package nl.tijsgroenendaal.queuemusicfacade.controllers.commands

data class AddSessionSongCommand(
    val album: String,
    val artists: List<String>,
    val name: String
)