package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class AddSpotifySessionSongCommand(
    val songId: String,
	val sessionId: UUID
)