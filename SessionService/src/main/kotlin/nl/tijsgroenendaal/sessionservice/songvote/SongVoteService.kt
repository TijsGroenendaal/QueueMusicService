package nl.tijsgroenendaal.sessionservice.songvote

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongUserVoteErrorCodes
import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.SongVoteModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.SongVoteRepository
import nl.tijsgroenendaal.sessionservice.songvote.jpa.VoteEnum
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SongVoteService(
    private val songVoteRepository: SongVoteRepository
) {

    fun vote(song: SongModel, userId: UUID, vote: VoteEnum): Pair<Int, SongVoteModel> {
        try {
            val oldUserVote = findBySongAndUser(song.id, userId)
            if (oldUserVote.vote == vote) {
                return Pair(0, oldUserVote)
            }

            val userVote = songVoteRepository.save(oldUserVote.apply { this.vote = vote })
            // Vote is multiplied to take into account that the difference between -1 and +1 is 2. And vice versa
            return Pair(vote.value * 2, userVote)
        } catch (e: BadRequestException) {
            val userVote = songVoteRepository.save(SongVoteModel.new(song, userId, vote))
            return Pair(vote.value, userVote)
        }
    }

    fun findBySong(song: UUID) = songVoteRepository.findBySongId(song)

    private fun findBySongAndUser(song: UUID, user: UUID): SongVoteModel {
        return songVoteRepository.findBySongIdAndUser(song, user)
            ?: throw BadRequestException(SessionSongUserVoteErrorCodes.VOTE_NOT_FOUND)
    }

}