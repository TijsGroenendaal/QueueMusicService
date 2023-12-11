package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class AcceptSessionSongCommand(
	val sessionId: UUID,
	val songId: UUID
)
