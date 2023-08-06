package nl.tijsgroenendaal.spotifyfacade.commands

import java.util.UUID

data class QueueTrackCommand(
    val hostId: UUID,
    val trackId: String,
)
