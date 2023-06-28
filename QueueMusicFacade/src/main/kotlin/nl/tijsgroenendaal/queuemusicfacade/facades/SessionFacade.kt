package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.SpotifyService
import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicfacade.services.*
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

private const val MAX_SECONDS_DURATION = 14400L
private const val MAX_ACTIVE_SESSION = 1
private const val MAX_USERS = 50

@Service
class SessionFacade(
    private val sessionService: SessionService,
    private val deviceLinkService: DeviceLinkService,
    private val sessionUserService: SessionUserService,
    private val spotifyService: SpotifyService,
    private val userService: UserService
) {

    fun createSession(command: CreateSessionCommand): SessionModel {
        val userId = getAuthenticationContextSubject()

        if (command.maxUsers > MAX_USERS)
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED)

        if (command.duration * 60 > MAX_SECONDS_DURATION)
            throw BadRequestException(SessionErrorCodes.DURATION_EXCEEDED)

        val activeSessions = sessionService.getActiveSessionsByUser(userId).size
        if (activeSessions >= MAX_ACTIVE_SESSION)
            throw BadRequestException(SessionErrorCodes.HOSTING_SESSIONS_EXCEEDED)

        val sessionCode = SessionModel.generateSessionCode()

        val playlist = if (command.autoplay) {
            spotifyService.createPlaylist(sessionCode)
        } else null

        val user = userService.findById(userId)

        return sessionService.createSession(nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand(
            playlist?.id,
            sessionCode,
            command.duration,
            user,
            command.maxUsers
        ))
    }

    fun joinSession(code: String): SessionUserModel {
        val deviceLink = deviceLinkService.getByUserId(getAuthenticationContextSubject())

        val session = sessionService.findSessionByCode(code)

        if (session.hasJoined(deviceLink.id))
            throw BadRequestException(SessionErrorCodes.ALREADY_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        if (!session.hasRoom())
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED)


        return sessionUserService.createNew(deviceLink, session)
    }

}