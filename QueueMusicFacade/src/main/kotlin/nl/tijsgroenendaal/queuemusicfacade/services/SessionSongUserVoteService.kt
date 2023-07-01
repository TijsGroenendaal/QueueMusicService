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

    fun vote(song: SessionSongModel, user: UserModel, vote: VoteEnum): SessionSongUserVoteModel {
        return try {
            val userVote = findBySongAndUser(song.id, getAuthenticationContextSubject())
            if (userVote.vote == vote) return userVote

            sessionSongUserVoteRepository.save(userVote.apply { this.vote = vote })
        }
        catch (e: BadRequestException) {
            sessionSongUserVoteRepository.save(SessionSongUserVoteModel.new(song, user, vote))
        }
    }

    private fun findBySongAndUser(song: UUID, user: UUID): SessionSongUserVoteModel {
        return sessionSongUserVoteRepository.findBySongIdAndUserId(song, user)
            ?: throw BadRequestException(SessionSongUserVoteErrorCodes.VOTE_NOT_FOUND)
    }

}