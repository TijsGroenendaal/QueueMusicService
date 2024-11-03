package nl.tijsgroenendaal.sessionservice.song

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel

import java.util.UUID

data class AddSongCommand(
    val trackId: String?,
    val trackAlbum: String,
    val trackName: String,
    val trackArtists: List<String>,
    val session: SessionModel,
    val userId: UUID
)
