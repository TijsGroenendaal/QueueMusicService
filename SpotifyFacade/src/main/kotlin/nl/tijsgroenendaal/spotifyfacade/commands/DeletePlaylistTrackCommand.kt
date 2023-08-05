package nl.tijsgroenendaal.spotifyfacade.commands

import java.util.UUID

data class DeletePlaylistTrackCommand(
    val userId: UUID,
    val trackId: String,
    val playlistId: String
)

data class DeletePlaylistTrackControllerCommand(
    val userId: UUID,
    val trackId: String,
)