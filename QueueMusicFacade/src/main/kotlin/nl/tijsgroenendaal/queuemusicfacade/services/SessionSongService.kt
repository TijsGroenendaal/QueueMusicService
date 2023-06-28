package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.SessionSongRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import org.springframework.beans.factory.annotation.Value

import org.springframework.stereotype.Service

import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class SessionSongService(
    private val sessionSongRepository: SessionSongRepository,
    @Value("\${queuemusic.session.song.timeout}")
    private val songTimeout: Duration
) {

    private fun canDeviceCreateSong(deviceLink: UserDeviceLinkModel) {
        val lowerBoundDateTime = LocalDateTime.now(ZoneOffset.UTC).minus(songTimeout)
        val sessionSongCount = sessionSongRepository.countByDeviceLinkIdAndCreatedAtAfter(deviceLink.id, lowerBoundDateTime)

        if (sessionSongCount >= 1)
            throw BadRequestException(SessionSongErrorCode.ADD_SONG_TIMEOUT_NOT_PASSED)
    }

    fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.isActive()) {
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)
        }

        canDeviceCreateSong(command.deviceLink)

        return sessionSongRepository.save(SessionSongModel.new(command))
    }

}