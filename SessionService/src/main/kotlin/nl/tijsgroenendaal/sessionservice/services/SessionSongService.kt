package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.services.commands.AddSessionSongCommand
import nl.tijsgroenendaal.sessionservice.entity.SessionSongModel
import nl.tijsgroenendaal.sessionservice.repositories.SessionSongRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class SessionSongService(
    private val sessionSongRepository: SessionSongRepository,
    @Value("\${queuemusic.session.song.timeout}")
    private val songTimeout: Duration
) {

    private fun verifyCanUserCreateSong(userId: UUID) {
        val lowerBoundDateTime = LocalDateTime.now(ZoneOffset.UTC).minus(songTimeout)
        val sessionSongCount = sessionSongRepository.countByUserAndCreatedAtAfter(userId, lowerBoundDateTime)

        if (sessionSongCount >= 1)
            throw BadRequestException(SessionSongErrorCode.ADD_SONG_TIMEOUT_NOT_PASSED)
    }

    fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.isActive()) {
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)
        }

        verifyCanUserCreateSong(command.userId)

        return sessionSongRepository.save(SessionSongModel.new(command))
    }

    fun getById(songId: UUID): SessionSongModel = sessionSongRepository.findById(songId)
        .orElseThrow { BadRequestException(SessionSongErrorCode.SESSION_SONG_NOT_FOUND) }

    fun updateVoteAggregate(songId: UUID, vote: Int): SessionSongModel {
        val song = getById(songId)

        song.votes += vote

        return sessionSongRepository.save(song)
    }

    fun save(song: SessionSongModel) = sessionSongRepository.save(song)

}