package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicservice.repositories.SessionSongRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode

import org.springframework.stereotype.Service

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

private val MIN_DURATION_TILL_NEXT_SONG = Duration.ofSeconds(900) // 15 minutes

@Service
class SessionSongService(
    private val sessionSongRepository: SessionSongRepository
) {

    private fun canDeviceCreateSong(deviceLink: UserDeviceLinkModel) {
        val lowerBoundDateTime = LocalDateTime.now(ZoneOffset.UTC).minus(MIN_DURATION_TILL_NEXT_SONG)
        val sessionSongCount = sessionSongRepository.countByDeviceLinkIdAndCreatedAtAfter(deviceLink.id, lowerBoundDateTime)

        if (sessionSongCount >= 1)
            throw BadRequestException(SessionSongErrorCode.ADD_SONG_TIMEOUT_NOT_PASSED, "Timeout from $lowerBoundDateTime not passed")
    }

    fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.isActive()) {
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED, "Cannot add SessionSong. Session ${command.session.id} has ended")
        }

        canDeviceCreateSong(command.deviceLink)

        return sessionSongRepository.save(SessionSongModel.new(command))
    }

}