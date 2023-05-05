package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.facade.SpotifyFacade
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.track.concatArtistNames
import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicservice.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicservice.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.queuemusicservice.services.DeviceLinkService
import nl.tijsgroenendaal.queuemusicservice.services.SessionService
import nl.tijsgroenendaal.queuemusicservice.services.SessionSongService
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class SessionSongFacade(
    private val sessionSongService: SessionSongService,
    private val sessionService: SessionService,
    private val spotifyFacade: SpotifyFacade,
    private val deviceLinkService: DeviceLinkService
) {

    fun addSpotifySessionSong(command: AddSpotifySessionSongCommand, sessionId: UUID): SessionSongModel {
        val track = spotifyFacade.getTrack(command.songId)

        return createSessionSong(AddSessionSongCommand(
            deviceLinkService.getByUserId(getAuthenticationContextSubject()),
            track.id,
            track.album.name,
            track.name,
            track.artists.concatArtistNames(),
            sessionService.findSessionById(sessionId)
        ))
    }

    fun addSessionSong(command: AddSessionSongControllerCommand, sessionId: UUID): SessionSongModel {
        return createSessionSong(AddSessionSongCommand(
            deviceLinkService.getByUserId(getAuthenticationContextSubject()),
            null,
            command.album,
            command.name,
            command.authors,
            sessionService.findSessionById(sessionId)
        ))
    }

    private fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.hasJoined(command.deviceLink.id))
            throw BadRequestException(SessionSongErrorCode.DEVICE_NOT_JOINED, "Device ${command.deviceLink.deviceId} has not joined")

        return sessionSongService.createSessionSong(command)
    }
}