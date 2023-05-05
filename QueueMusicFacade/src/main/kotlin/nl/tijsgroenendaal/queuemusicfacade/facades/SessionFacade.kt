package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.entity.QueueMusicSessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicfacade.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.queuemusicfacade.services.*
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes

import org.springframework.stereotype.Service

private const val MAX_SECONDS_DURATION = 14400L
private const val MAX_ACTIVE_SESSION = 1
private const val MAX_USERS = 50

@Service
class SessionFacade(
    private val sessionService: SessionService,
    private val userLinkService: UserLinkService,
    private val deviceLinkService: DeviceLinkService,
    private val sessionUserService: SessionUserService,
    private val spotifyApiClientService: SpotifyApiClientService
) {

    fun createSession(command: CreateSessionCommand): QueueMusicSessionModel {
        val userId = getAuthenticationContextSubject()

        if (command.maxUsers > MAX_USERS)
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED, "Max users limited by $MAX_USERS, tried ${command.maxUsers}")

        if (command.duration * 60 > MAX_SECONDS_DURATION)
            throw BadRequestException(SessionErrorCodes.DURATION_EXCEEDED, "Duration ${command.duration * 60} exceeds max $MAX_SECONDS_DURATION")

        val activeSessions = sessionService.getActiveSessionsByUser(userId).size
        if (activeSessions >= MAX_ACTIVE_SESSION)
            throw BadRequestException(SessionErrorCodes.HOSTING_SESSIONS_EXCEEDED, "Max active sessions ($activeSessions) reached for user $userId")

        val userLink = userLinkService.findByUserId(userId)

        val sessionCode = QueueMusicSessionModel.generateSessionCode()

        val playlist = spotifyApiClientService.createPlaylist(
            userLink,
            sessionCode
        )

        return sessionService.createSession(nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand(
            playlist.id,
            sessionCode,
            command.duration,
            userLink.userModel,
            command.maxUsers
        ))
    }

    fun joinSession(code: String): SessionUserModel {
        val deviceLink = deviceLinkService.getByUserId(getAuthenticationContextSubject())

        val session = sessionService.findSessionByCode(code)

        if (session.hasJoined(deviceLink.id))
            throw BadRequestException(SessionErrorCodes.ALREADY_JOINED, "Device ${deviceLink.deviceId} has already joined Session ${session.code} ")

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED, "Session ${session.code} has ended")

        if (!session.hasRoom())
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED, "Session ${session.code} has reached max users")


        return sessionUserService.createNew(deviceLink, session)
    }

}