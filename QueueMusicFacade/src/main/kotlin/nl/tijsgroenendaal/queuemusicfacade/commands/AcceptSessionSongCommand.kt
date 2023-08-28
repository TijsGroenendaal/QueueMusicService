package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class AcceptSessionSongCommand(
	val songId: UUID,
	val sessionId: UUID
)
