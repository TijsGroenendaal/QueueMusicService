package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands

import java.util.UUID

data class AddPlaylistTrackCommand(
    val userId: UUID,
    val trackId: String,
    val position: Int
)
