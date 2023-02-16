package nl.tijsgroenendaal.queuemusicservice.commands

import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSession
import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLink

data class AddSessionSongCommand(
    val deviceLink: UserDeviceLink,
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: String,
    val session: QueueMusicSession
)
