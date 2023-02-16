package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.track.concatArtistNames
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicservice.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicservice.entity.SessionSong
import nl.tijsgroenendaal.queuemusicservice.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.queuemusicservice.services.DeviceLinkService
import nl.tijsgroenendaal.queuemusicservice.services.SessionService
import nl.tijsgroenendaal.queuemusicservice.services.SessionSongService

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class SessionSongFacade(
    private val sessionSongService: SessionSongService,
    private val sessionService: SessionService,
    private val spotifyApiClientService: SpotifyApiClientService,
    private val deviceLinkService: DeviceLinkService
) {

    fun addSpotifySessionSong(command: AddSpotifySessionSongCommand, sessionId: UUID): SessionSong {
        val track = spotifyApiClientService.getTrack(command.songId)

        return sessionSongService.createSessionSong(AddSessionSongCommand(
            deviceLinkService.getByUserId(getAuthenticationContextSubject()),
            track.id,
            track.album.name,
            track.name,
            track.artists.concatArtistNames(),
            sessionService.findSessionById(sessionId)
        ))
    }

    fun addSessionSong(command: AddSessionSongControllerCommand, sessionId: UUID): SessionSong {
        return sessionSongService.createSessionSong(AddSessionSongCommand(
            deviceLinkService.getByUserId(getAuthenticationContextSubject()),
            null,
            command.album,
            command.name,
            command.authors,
            sessionService.findSessionById(sessionId)
        ))
    }
}