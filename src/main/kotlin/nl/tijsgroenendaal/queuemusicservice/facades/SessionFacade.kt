package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicservice.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSession
import nl.tijsgroenendaal.queuemusicservice.exceptions.BadRequestException
import nl.tijsgroenendaal.queuemusicservice.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.queuemusicservice.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.queuemusicservice.services.SessionService
import nl.tijsgroenendaal.queuemusicservice.services.UserLinkService

import org.springframework.stereotype.Service

private const val MAX_SECONDS_DURATION = 14400L
private const val MAX_ACTIVE_SESSION = 1

@Service
class SessionFacade(
    private val sessionService: SessionService,
    private val userLinkService: UserLinkService,
    private val spotifyApiClientService: SpotifyApiClientService
) {

    fun createSession(command: CreateSessionCommand): QueueMusicSession {
        val userId = getAuthenticationContextSubject()

        if (command.duration * 60 > MAX_SECONDS_DURATION)
            throw BadRequestException(SessionErrorCodes.DURATION_EXCEEDED, "Duration ${command.duration * 60} exceeds max $MAX_SECONDS_DURATION")

        val activeSessions = sessionService.getActiveSessionsByUser(userId).size
        if (activeSessions >= MAX_ACTIVE_SESSION)
            throw BadRequestException(SessionErrorCodes.HOSTING_SESSIONS_EXCEEDED, "Max active sessions ($activeSessions) reached for user $userId")

        val userLink = userLinkService.findByUserId(userId)

        val sessionCode = QueueMusicSession.generateSessionCode()

        val playlist = spotifyApiClientService.createPlaylist(
            userLink,
            sessionCode
        )

        return sessionService.createSession(nl.tijsgroenendaal.queuemusicservice.services.commands.CreateSessionCommand(
            playlist.id,
            sessionCode,
            command.duration,
            userLink.userModel
        ))
    }

}