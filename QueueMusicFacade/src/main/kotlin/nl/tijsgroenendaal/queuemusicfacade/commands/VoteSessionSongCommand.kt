package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class VoteSessionSongCommand(
	val sessionId: UUID,
	val songId: UUID,
	val vote: String
)