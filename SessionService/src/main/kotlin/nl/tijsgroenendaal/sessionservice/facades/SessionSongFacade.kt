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
import nl.tijsgroenendaal.sessionservice.services.UserEventService
import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTask
import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTaskSong
import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTaskType
import nl.tijsgroenendaal.sessionservice.services.commands.UserEventTaskVoter

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
    private val autoQueueService: AutoQueueService,
    private val userEventService: UserEventService
) {

    fun voteSessionSong(sessionId: UUID, songId: UUID, vote: VoteEnum, userId: UUID): SessionSongUserVoteModel {
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

        launch { createUserEventMessage(UserEventTaskType.VOTE, song, sessionSongUserVoteService.findBySong(songId)) }

        return userVote.second
    }

    fun deleteSessionSong(sessionId: UUID, songId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isHost(userId))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = sessionSongService.getById(songId).apply {
            this.state = SongState.DELETED
        }
        sessionSongService.save(song)

        launch { createUserEventMessage(UserEventTaskType.REMOVE, song, listOf()) }
    }

    fun acceptSessionSong(sessionId: UUID, songId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)
        val song = sessionSongService.getById(songId)

        if (!session.isHost(userId))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        acceptSessionSong(session, song)
    }

    fun createSessionSong(command: AddSessionSongCommand, userId: UUID): SessionSongModel {
        val session = sessionService.findSessionById(command.sessionId)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        val sessionSong = sessionSongService.createSessionSong(ServiceAddSessionSongCommand(
            command.trackId,
            command.trackAlbum,
            command.trackName,
            command.trackArtists,
            session,
            userId
        ))

        launch { createUserEventMessage(UserEventTaskType.ADD, sessionSong, listOf()) }
        return sessionSong
    }

    private fun createAutoplayMessage(trackId: String, hostId: UUID) {
        autoQueueService.publish(AutoplayUpdateTask(
            hostId,
            trackId
        ))
    }

    private fun createUserEventMessage(type: UserEventTaskType, song: SessionSongModel, voters: List<SessionSongUserVoteModel>) {
        userEventService.publish(UserEventTask(
            song.session.id,
            type,
            UserEventTaskSong(
                song.id,
                song.title,
                song.authors,
                song.album
            ),
            song.user,
            voters.map {
                UserEventTaskVoter(
                    it.user,
                    it.vote
                )
            }
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
            sessionSongService.save(song.apply { this.state = SongState.PLAYED })
            createAutoplayMessage(song.trackId, session.host)
        }

        launch { createUserEventMessage(UserEventTaskType.ACCEPT, song, sessionSongUserVoteService.findBySong(song.id)) }
    }

    private fun launch(task: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            task.invoke()
        }
    }
}