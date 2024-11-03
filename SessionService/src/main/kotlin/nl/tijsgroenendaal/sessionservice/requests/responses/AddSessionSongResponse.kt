package nl.tijsgroenendaal.sessionservice.requests.responses


import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel

import java.time.Instant
import java.util.UUID

data class AddSessionSongResponse(
    val id: UUID,
    val user: AddSessionSongResponseUser,
    val session: AddSessionSongResponseSession,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val createdAt: Instant,
) {
    constructor(songModel: SongModel) : this(
        songModel.id,
        AddSessionSongResponseUser(songModel.user),
        AddSessionSongResponseSession(songModel.session),
        songModel.trackId,
        songModel.title,
        songModel.album,
        songModel.authors,
        songModel.createdAt
    )
}

data class AddSessionSongResponseUser(
    val id: UUID,
)

data class AddSessionSongResponseSession(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String
) {
    constructor(session: SessionModel) : this(
        session.id,
        session.createdAt,
        session.endAt,
        session.code
    )
}
