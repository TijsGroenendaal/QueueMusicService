package nl.tijsgroenendaal.spotifyfacade.commands

import java.util.UUID

data class AddPlaylistTrackCommand(
    val userId: UUID,
    val trackId: String,
    val position: Int,
    val playlistId: String
)

data class AddPlaylistTrackControllerCommand(
    val userId: UUID,
    val trackId: String,
    val position: Int
)
