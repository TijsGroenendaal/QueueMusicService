package nl.tijsgroenendaal.queuemusicfacade.commands

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel

data class AddSessionSongCommand(
    val deviceLink: UserDeviceLinkModel,
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: String,
    val session: SessionModel
)
