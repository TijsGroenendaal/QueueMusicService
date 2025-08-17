package nl.tijsgroenendaal.sessionservice.song

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import nl.tijsgroenendaal.sessionservice.queue.AutoQueueService
import nl.tijsgroenendaal.sessionservice.queue.AutoplayUpdateTask
import nl.tijsgroenendaal.sessionservice.queue.UserEventService
import nl.tijsgroenendaal.sessionservice.queue.UserEventTask
import nl.tijsgroenendaal.sessionservice.queue.UserEventTaskType
import nl.tijsgroenendaal.sessionservice.requests.responses.GetSongsRequestResponse
import nl.tijsgroenendaal.sessionservice.session.SessionService
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import nl.tijsgroenendaal.sessionservice.song.jpa.SongState
import nl.tijsgroenendaal.sessionservice.songvote.SongVoteService
import nl.tijsgroenendaal.sessionservice.songvote.jpa.SongVoteModel
import nl.tijsgroenendaal.sessionservice.songvote.jpa.VoteEnum
import org.springframework.stereotype.Service
import java.util.UUID
import nl.tijsgroenendaal.sessionservice.song.AddSongCommand as ServiceAddSessionSongCommand

@Service
class SongFacade(
    private val songService: SongService,
    private val sessionService: SessionService,
    private val songVoteService: SongVoteService,
    private val autoQueueService: AutoQueueService,
    private val userEventService: UserEventService
) {

    fun voteSong(sessionId: UUID, songId: UUID, vote: VoteEnum, userId: UUID): SongVoteModel {
        val session = sessionService.findSessionById(sessionId)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = songService.getById(songId)

        if (song.state == SongState.PLAYED)
            throw BadRequestException(SessionSongErrorCode.SONG_ALREADY_QUEUED)

        val (difference, voteEntity) = songVoteService.vote(song, userId, vote)

        // Short circuit because nothing has changed.
        if (difference == 0) {
            return voteEntity
        }

        val updatedSong = songService.updateVoteAggregate(songId, difference)

        if (session.autoplayAcceptance != null && updatedSong.votes >= session.autoplayAcceptance) {
            acceptSong(session, updatedSong)
        }

        launch { createUserEventMessage(UserEventTaskType.VOTE, song, songVoteService.findBySong(songId)) }

        return voteEntity
    }

    fun deleteSong(sessionId: UUID, songId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isHost(userId))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        val song = songService.getById(songId).apply {
            this.state = SongState.DELETED
        }
        songService.save(song)

        launch { createUserEventMessage(UserEventTaskType.REMOVE, song, listOf()) }
    }

    fun acceptSong(sessionId: UUID, songId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)
        val song = songService.getById(songId)

        if (!session.isHost(userId))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        acceptSong(session, song)
    }

    fun createSong(command: AddSongRequest, userId: UUID): SongModel {
        val session = sessionService.findSessionById(command.sessionId)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        val sessionSong = songService.createSessionSong(
            ServiceAddSessionSongCommand(
                command.trackId,
                command.trackAlbum,
                command.trackName,
                command.trackArtists,
                session,
                userId
            )
        )

        launch { createUserEventMessage(UserEventTaskType.ADD, sessionSong, listOf()) }
        return sessionSong
    }

    fun getSongs(sessionId: UUID, userId: UUID): List<GetSongsRequestResponse> {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)
        if (!session.partOfSession(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        return songService.getSongs(sessionId).map {
            GetSongsRequestResponse(
                it.id,
                it.user,
                it.trackId,
                it.title,
                it.album,
                it.authors,
                it.votes,
                it.createdAt,
                it.state
            )
        }
    }

    private fun createAutoplayMessage(trackId: String, hostId: UUID) {
        autoQueueService.publish(
            AutoplayUpdateTask(
                hostId,
                trackId
            )
        )
    }

    private fun createUserEventMessage(
        type: UserEventTaskType,
        song: SongModel,
        voters: List<SongVoteModel>
    ) {
        val votersInResponse = when (type) {
            UserEventTaskType.VOTE -> voters
            else -> emptyList()
        }

        userEventService.publish(
            UserEventTask(
                song,
                type,
                votersInResponse
            )
        )
    }

    private fun acceptSong(session: SessionModel, song: SongModel) {
        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        if (song.state == SongState.DELETED)
            throw BadRequestException(SessionSongErrorCode.SESSION_SONG_NOT_FOUND)

        if (song.state == SongState.PLAYED)
            throw BadRequestException(SessionSongErrorCode.SONG_ALREADY_QUEUED)

        if (session.autoplayAcceptance != null && song.trackId != null) {
            songService.save(song.apply { this.state = SongState.PLAYED })
            createAutoplayMessage(song.trackId, session.host)
        }

        launch {
            createUserEventMessage(
                UserEventTaskType.ACCEPT,
                song,
                songVoteService.findBySong(song.id)
            )
        }
    }

    private fun launch(task: () -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            task.invoke()
        }
    }
}