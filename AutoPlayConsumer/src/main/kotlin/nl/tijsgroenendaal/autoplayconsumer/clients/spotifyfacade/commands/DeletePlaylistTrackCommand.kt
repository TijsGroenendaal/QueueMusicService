package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.commands

import java.util.UUID

data class DeletePlaylistTrackCommand(
    val userId: UUID,
    val trackId: String
)
