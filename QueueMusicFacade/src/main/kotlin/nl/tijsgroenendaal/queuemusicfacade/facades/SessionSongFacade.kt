package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.concatArtistNames
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.SpotifyService
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongControllerCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSpotifySessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongUserVoteModel
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.SongState
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.VoteEnum
import nl.tijsgroenendaal.queuemusicfacade.services.AutoQueueService
import nl.tijsgroenendaal.queuemusicfacade.services.SessionService
import nl.tijsgroenendaal.queuemusicfacade.services.SessionSongService
import nl.tijsgroenendaal.queuemusicfacade.services.SessionSongUserVoteService
import nl.tijsgroenendaal.queuemusicfacade.services.commands.AutoplayUpdateTask
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Service
class SessionSongFacade(
    private val sessionSongService: SessionSongService,
    private val sessionService: SessionService,
    private val spotifyService: SpotifyService,
    private val sessionSongUserVoteService: SessionSongUserVoteService,
    private val autoQueueService: AutoQueueService
) {

    fun addSpotifySessionSong(command: AddSpotifySessionSongCommand, sessionId: UUID): SessionSongModel {
        val track = spotifyService.getTrack(command.songId)
        val userId = getAuthenticationContextSubject()

        return createSessionSong(AddSessionSongCommand(
            userId,
            track.id,
            track.album.name,
            track.name,
            track.artists.concatArtistNames(),
            sessionService.findSessionById(sessionId)
        ))
    }

    fun addSessionSong(command: AddSessionSongControllerCommand, sessionId: UUID): SessionSongModel {
        val userId = getAuthenticationContextSubject()

        return createSessionSong(AddSessionSongCommand(
            userId,
            null,
            command.album,
            command.name,
            command.authors,
            sessionService.findSessionById(sessionId)
        ))
    }

    fun voteSessionSong(sessionId: UUID, songId: UUID, vote: VoteEnum): SessionSongUserVoteModel {
        val userId = getAuthenticationContextSubject()
        val session = sessionService.findSessionById(sessionId)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = sessionSongService.getById(songId)

        if (song.state != SongState.QUEUED)
            throw BadRequestException(SessionSongErrorCode.SONG_ALREADY_QUEUED)

        val userVote = sessionSongUserVoteService.vote(song, userId, vote)

        // Short circuit because nothing has changed.
        if (userVote.first == 0) {
            return userVote.second
        }

        val updatedSong = sessionSongService.updateVoteAggregate(songId, userVote.first)

        if (session.autoplayAcceptance != null && song.trackId != null && updatedSong.votes >= session.autoplayAcceptance) {
            CoroutineScope(Dispatchers.IO).launch {
                sessionSongService.save(updatedSong.apply { this.state = SongState.PLAYED })
                createAutoplayMessage(song.trackId, session.host)
            }
        }

        return userVote.second
    }

    fun deleteSessionSong(sessionId: UUID, songId: UUID) {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isHost(getAuthenticationContextSubject()))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = sessionSongService.getById(songId).apply {
            this.state = SongState.DELETED
        }
        sessionSongService.save(song)
    }

    private fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        if (!command.session.hasJoined(command.userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        return sessionSongService.createSessionSong(command)
    }

    private fun createAutoplayMessage(trackId: String, hostId: UUID) {
        autoQueueService.publish(AutoplayUpdateTask(
            hostId,
            trackId
        ))
    }
}