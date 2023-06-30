package nl.tijsgroenendaal.queuemusicfacade.commands

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel

data class AddSessionSongCommand(
    val user: UserModel,
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: String,
    val session: SessionModel
)
