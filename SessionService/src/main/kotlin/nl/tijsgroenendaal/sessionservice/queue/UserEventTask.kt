package nl.tijsgroenendaal.sessionservice.queue

import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.SongVoteModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.VoteEnum

import java.util.UUID

data class UserEventTask(
    val sessionId: UUID,
    val type: UserEventTaskType,
    val song: UserEventTaskSong,
    val user: UUID,
    val voters: List<UserEventTaskVoter>
) {
	constructor(song: SongModel, type: UserEventTaskType, voters: List<SongVoteModel>) : this(
		song.session.id,
		type,
		UserEventTaskSong(song.id),
		song.user,
		voters.map { UserEventTaskVoter(it.user, it.vote) }
	)
}

enum class UserEventTaskType {
    ADD,
    REMOVE,
    ACCEPT,
    VOTE;
}

data class UserEventTaskSong(
    val id: UUID,
)

data class UserEventTaskVoter(
    val user: UUID,
    val type: VoteEnum
)