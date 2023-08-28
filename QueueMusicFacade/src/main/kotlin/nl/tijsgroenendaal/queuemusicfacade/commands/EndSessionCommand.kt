package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class EndSessionCommand(
	val sessionId: UUID
)
