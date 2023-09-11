package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class DeleteSessionSongCommand(
	val songId: UUID,
	val sessionId: UUID
)
