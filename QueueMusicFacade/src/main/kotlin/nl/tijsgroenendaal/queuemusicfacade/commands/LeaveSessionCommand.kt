package nl.tijsgroenendaal.queuemusicfacade.commands

import java.util.UUID

data class LeaveSessionCommand(
	val sessionId: UUID
)
