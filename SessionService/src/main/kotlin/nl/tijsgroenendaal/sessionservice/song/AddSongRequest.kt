package nl.tijsgroenendaal.sessionservice.song

import java.util.UUID

data class AddSongRequest(
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: List<String>,
    val sessionId: UUID,
)
