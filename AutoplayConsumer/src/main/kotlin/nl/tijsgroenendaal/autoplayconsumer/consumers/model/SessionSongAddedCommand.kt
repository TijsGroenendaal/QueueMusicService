package nl.tijsgroenendaal.autoplayconsumer.consumers.model

data class SessionSongAddedCommand(
    val songId: String,
    val sessionId: String,
    val index: Int
)
