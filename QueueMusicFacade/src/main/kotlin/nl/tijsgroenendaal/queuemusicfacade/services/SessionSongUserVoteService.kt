package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongUserVoteModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.VoteEnum
import nl.tijsgroenendaal.queuemusicfacade.repositories.SessionSongUserVoteRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongUserVoteErrorCodes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SessionSongUserVoteService(
    private val sessionSongUserVoteRepository: SessionSongUserVoteRepository
) {

    fun vote(song: SessionSongModel, user: UserModel, vote: VoteEnum): Pair<Int, SessionSongUserVoteModel> {
        try {
            val oldUserVote = findBySongAndUser(song.id, getAuthenticationContextSubject())
            if (oldUserVote.vote == vote) {
                return Pair(0, oldUserVote)
            }

            val userVote = sessionSongUserVoteRepository.save(oldUserVote.apply { this.vote = vote })
            // Vote is multiplied to take into account that the difference from +1 to -1, 2 is.
            return Pair(vote.value * 2, userVote)
        }
        catch (e: BadRequestException) {
            val userVote = sessionSongUserVoteRepository.save(SessionSongUserVoteModel.new(song, user, vote))
            return Pair(vote.value, userVote)
        }
    }

    private fun findBySongAndUser(song: UUID, user: UUID): SessionSongUserVoteModel {
        return sessionSongUserVoteRepository.findBySongIdAndUserId(song, user)
            ?: throw BadRequestException(SessionSongUserVoteErrorCodes.VOTE_NOT_FOUND)
    }

}