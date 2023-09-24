package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.entity.SessionSongModel
import nl.tijsgroenendaal.sessionservice.entity.SessionSongUserVoteModel
import nl.tijsgroenendaal.sessionservice.entity.enums.VoteEnum
import nl.tijsgroenendaal.sessionservice.repositories.SessionSongUserVoteRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongUserVoteErrorCodes

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SessionSongUserVoteService(
    private val sessionSongUserVoteRepository: SessionSongUserVoteRepository
) {

    fun vote(song: SessionSongModel, userId: UUID, vote: VoteEnum): Pair<Int, SessionSongUserVoteModel> {
        try {
            val oldUserVote = findBySongAndUser(song.id, userId)
            if (oldUserVote.vote == vote) {
                return Pair(0, oldUserVote)
            }

            val userVote = sessionSongUserVoteRepository.save(oldUserVote.apply { this.vote = vote })
            // Vote is multiplied to take into account that the difference between -1 and +1 is 2. And vice versa
            return Pair(vote.value * 2, userVote)
        }
        catch (e: BadRequestException) {
            val userVote = sessionSongUserVoteRepository.save(SessionSongUserVoteModel.new(song, userId, vote))
            return Pair(vote.value, userVote)
        }
    }

    fun findBySong(song: UUID) = sessionSongUserVoteRepository.findBySongId(song)

    private fun findBySongAndUser(song: UUID, user: UUID): SessionSongUserVoteModel {
        return sessionSongUserVoteRepository.findBySongIdAndUser(song, user)
            ?: throw BadRequestException(SessionSongUserVoteErrorCodes.VOTE_NOT_FOUND)
    }

}