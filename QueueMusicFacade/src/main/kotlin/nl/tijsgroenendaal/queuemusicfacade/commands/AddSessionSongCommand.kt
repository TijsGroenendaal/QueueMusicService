package nl.tijsgroenendaal.queuemusicfacade.commands

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import java.util.UUID

data class AddSessionSongCommand(
    val userId: UUID,
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: String,
    val session: SessionModel
)
