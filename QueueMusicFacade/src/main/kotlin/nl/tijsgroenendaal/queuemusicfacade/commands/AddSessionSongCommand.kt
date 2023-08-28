package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class AddSessionSongCommand(
    val trackId: String?,
    val album: String,
    val name: String,
    val artists: List<String>,
    val sessionId: UUID
)
