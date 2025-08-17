package nl.tijsgroenendaal.sessionservice.song

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import nl.tijsgroenendaal.sessionservice.song.jpa.SongRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant
import java.util.UUID

@Service
class SongService(
    private val songRepository: SongRepository,
    @Value("\${queuemusic.session.song.timeout}")
    private val songTimeout: Duration
) {

    private fun verifyCanUserCreateSong(userId: UUID) {
        val lowerBoundDateTime = Instant.now().minus(songTimeout)
        val sessionSongCount = songRepository.countByUserAndCreatedAtAfter(userId, lowerBoundDateTime)

        if (sessionSongCount >= 1)
            throw BadRequestException(SessionSongErrorCode.ADD_SONG_TIMEOUT_NOT_PASSED)
    }

    fun createSessionSong(command: AddSongCommand): SongModel {
        if (!command.session.isActive()) {
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)
        }

        verifyCanUserCreateSong(command.userId)

        return songRepository.save(SongModel.new(command))
    }

    fun getById(songId: UUID): SongModel = songRepository.findById(songId)
        .orElseThrow { BadRequestException(SessionSongErrorCode.SESSION_SONG_NOT_FOUND) }

    fun updateVoteAggregate(songId: UUID, vote: Int): SongModel {
        val song = getById(songId)

        song.votes += vote

        return songRepository.save(song)
    }

    fun save(song: SongModel) = songRepository.save(song)

    fun getSongs(sessionId: UUID): List<SongModel> = songRepository.findAllBySessionId(sessionId)

}