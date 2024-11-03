package nl.tijsgroenendaal.sessionservice.queue

import java.util.UUID

data class AutoplayUpdateTask(
    val hostId: UUID,
    val trackId: String
)