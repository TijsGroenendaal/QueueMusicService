package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.concatArtistNames
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.SpotifyService
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongUserVoteModel
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.VoteEnum
import nl.tijsgroenendaal.queuemusicfacade.services.SessionService
import nl.tijsgroenendaal.queuemusicfacade.services.SessionSongService
import nl.tijsgroenendaal.queuemusicfacade.services.SessionSongUserVoteService
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class SessionSongFacade(
    private val sessionSongService: SessionSongService,
    private val sessionService: SessionService,
    private val spotifyService: SpotifyService,
    private val deviceLinkService: UserService,
    private val sessionSongUserVoteService: SessionSongUserVoteService
) {

    fun addSpotifySessionSong(command: AddSpotifySessionSongCommand, sessionId: UUID): SessionSongModel {
        val track = spotifyService.getTrack(command.songId)

        return createSessionSong(AddSessionSongCommand(
            deviceLinkService.findById(getAuthenticationContextSubject()),
            track.id,
            track.album.name,
            track.name,
            track.artists.concatArtistNames(),
            sessionService.findSessionById(sessionId)
        ))
    }

    fun addSessionSong(command: AddSessionSongControllerCommand, sessionId: UUID): SessionSongModel {
        return createSessionSong(AddSessionSongCommand(
            deviceLinkService.findById(getAuthenticationContextSubject()),
            null,
            command.album,
            command.name,
            command.authors,
            sessionService.findSessionById(sessionId)
        ))
    }

    fun voteSessionSong(sessionId: UUID, songId: UUID, vote: VoteEnum): SessionSongUserVoteModel {
        val session = sessionService.findSessionById(sessionId)

        val user = session.getUser(getAuthenticationContextSubject())
            ?: throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = sessionSongService.getById(songId)

        return sessionSongUserVoteService.vote(song, user, vote)
    }

    private fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.hasJoined(command.user))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        return sessionSongService.createSessionSong(command)
    }
}