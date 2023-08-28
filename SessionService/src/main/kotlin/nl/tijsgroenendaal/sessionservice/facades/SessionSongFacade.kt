package nl.tijsgroenendaal.sessionservice.facades

import nl.tijsgroenendaal.sessionservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.sessionservice.entity.SessionModel
import nl.tijsgroenendaal.sessionservice.entity.SessionSongModel
import nl.tijsgroenendaal.sessionservice.entity.SessionSongUserVoteModel
import nl.tijsgroenendaal.sessionservice.entity.enums.SongState
import nl.tijsgroenendaal.sessionservice.entity.enums.VoteEnum
import nl.tijsgroenendaal.sessionservice.services.SessionService
import nl.tijsgroenendaal.sessionservice.services.SessionSongService
import nl.tijsgroenendaal.sessionservice.services.SessionSongUserVoteService
import nl.tijsgroenendaal.sessionservice.services.AutoQueueService
import nl.tijsgroenendaal.sessionservice.services.commands.AutoplayUpdateTask
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

import java.util.UUID
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

import nl.tijsgroenendaal.sessionservice.services.commands.AddSessionSongCommand as ServiceAddSessionSongCommand

@Service
class SessionSongFacade(
    private val sessionSongService: SessionSongService,
    private val sessionService: SessionService,
    private val sessionSongUserVoteService: SessionSongUserVoteService,
    private val autoQueueService: AutoQueueService
) {

    fun voteSessionSong(sessionId: UUID, songId: UUID, vote: VoteEnum): SessionSongUserVoteModel {
        val userId = getAuthenticationContextSubject()
        val session = sessionService.findSessionById(sessionId)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = sessionSongService.getById(songId)

        if (song.state == SongState.PLAYED)
            throw BadRequestException(SessionSongErrorCode.SONG_ALREADY_QUEUED)

        val userVote = sessionSongUserVoteService.vote(song, userId, vote)

        // Short circuit because nothing has changed.
        if (userVote.first == 0) {
            return userVote.second
        }

        val updatedSong = sessionSongService.updateVoteAggregate(songId, userVote.first)

        if (session.autoplayAcceptance != null && updatedSong.votes >= session.autoplayAcceptance) {
            acceptSessionSong(session, updatedSong)
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

    fun acceptSessionSong(sessionId: UUID, songId: UUID) {
        val session = sessionService.findSessionById(sessionId)
        val song = sessionSongService.getById(songId)

        if (!session.isHost(getAuthenticationContextSubject()))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        acceptSessionSong(session, song)
    }

    fun createSessionSong(command: AddSessionSongCommand): SessionSongModel {
        val session = sessionService.findSessionById(command.sessionId)

        val userId = getAuthenticationContextSubject()

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        return sessionSongService.createSessionSong(ServiceAddSessionSongCommand(
            command.trackId,
            command.trackAlbum,
            command.trackName,
            command.trackArtists,
            session,
            userId
        ))
    }

    private fun createAutoplayMessage(trackId: String, hostId: UUID) {
        autoQueueService.publish(AutoplayUpdateTask(
            hostId,
            trackId
        ))
    }

    private fun acceptSessionSong(session: SessionModel, song: SessionSongModel) {
        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        if (song.state == SongState.DELETED)
            throw BadRequestException(SessionSongErrorCode.SESSION_SONG_NOT_FOUND)

        if (song.state == SongState.PLAYED)
            throw BadRequestException(SessionSongErrorCode.SONG_ALREADY_QUEUED)

        if (session.autoplayAcceptance != null && song.trackId != null) {
            CoroutineScope(Dispatchers.IO).launch {
                sessionSongService.save(song.apply { this.state = SongState.PLAYED })
                createAutoplayMessage(song.trackId, session.host)
            }
        }
    }
}