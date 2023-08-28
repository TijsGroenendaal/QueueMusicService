package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import java.time.LocalDateTime

import java.util.UUID

data class VoteSessionSongCommandResponse(
    val id: UUID,
    val song: VoteSessionSongCommandResponseSong,
    val user: VoteSessionSongCommandResponseUser,
    val vote: String
)

data class VoteSessionSongCommandResponseSong(
    val id: UUID,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val createdAt: LocalDateTime,
)

data class VoteSessionSongCommandResponseUser(
    val id: UUID
)
