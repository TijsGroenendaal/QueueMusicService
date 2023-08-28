package nl.tijsgroenendaal.sessionservice.services.commands

import java.util.UUID

data class AutoplayUpdateTask(
    val hostId: UUID,
    val trackId: String
)