package nl.tijsgroenendaal.sessionservice.services.commands

import nl.tijsgroenendaal.sessionservice.entity.enums.VoteEnum

import java.util.UUID

data class UserEventTask(
	val sessionId: UUID,
	val type: UserEventTaskType,
	val song: UserEventTaskSong,
	val user: UUID,
	val voters: List<UserEventTaskVoter>
)

enum class UserEventTaskType {
	ADD,
	REMOVE,
	ACCEPT,
	VOTE;
}

data class UserEventTaskSong(
	val id: UUID,
	val title: String,
	val authors: String,
	val album: String,
)

data class UserEventTaskVoter(
	val user: UUID,
	val type: VoteEnum
)