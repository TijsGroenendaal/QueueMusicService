package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses

import java.time.Instant

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
    val createdAt: Instant,
)

data class VoteSessionSongCommandResponseUser(
    val id: UUID
)
