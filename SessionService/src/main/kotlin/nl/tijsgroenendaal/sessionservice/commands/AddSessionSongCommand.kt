package nl.tijsgroenendaal.sessionservice.commands

import java.util.UUID

data class AddSessionSongCommand(
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: List<String>,
	val sessionId: UUID,
)
