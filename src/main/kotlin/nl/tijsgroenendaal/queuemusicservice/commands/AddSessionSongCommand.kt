package nl.tijsgroenendaal.queuemusicservice.commands

import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSessionModel
import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLinkModel

data class AddSessionSongCommand(
    val deviceLink: UserDeviceLinkModel,
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: String,
    val session: QueueMusicSessionModel
)
