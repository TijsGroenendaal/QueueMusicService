package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.SongVoteModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.VoteEnum
import java.time.Instant

import java.util.UUID

data class VoteSessionSongResponse(
    val id: UUID,
    val song: VoteSessionSongResponseSong,
    val user: VoteSessionSongResponseUser,
    val vote: VoteEnum
) {

    constructor(userVote: SongVoteModel) : this(
        userVote.id,
        VoteSessionSongResponseSong(
            userVote.song
        ),
        VoteSessionSongResponseUser(
            userVote.user
        ),
        userVote.vote
    )
}

data class VoteSessionSongResponseSong(
    val id: UUID,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val createdAt: Instant,
) {
    constructor(song: SongModel) : this(
        song.id,
        song.trackId,
        song.title,
        song.album,
        song.authors,
        song.createdAt
    )
}

data class VoteSessionSongResponseUser(
    val id: UUID
)
