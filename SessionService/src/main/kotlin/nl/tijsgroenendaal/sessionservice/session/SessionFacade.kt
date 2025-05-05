package nl.tijsgroenendaal.sessionservice.session

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.sessionuser.SessionUserService
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserModel
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID


private const val MAX_SECONDS_DURATION = 14400L
private const val MAX_USERS = 50

@Service
class SessionFacade(
    private val sessionService: SessionService,
    private val sessionUserService: SessionUserService,
) {

    fun createSession(command: CreateSessionControllerRequest, userId: UUID): SessionModel {
        if (command.maxUsers > MAX_USERS)
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED)

        if (command.duration * 60 > MAX_SECONDS_DURATION)
            throw BadRequestException(SessionErrorCodes.DURATION_EXCEEDED)

        if (command.autoplay != null && command.autoplay.acceptance < 1)
            throw BadRequestException(SessionErrorCodes.NEGATIVE_AUTOPLAY_ACCEPTANCE)

        val activeSessions = sessionService.findByUser(userId)
        if (activeSessions != null) {
            if (activeSessions.isHost(userId))
                throw BadRequestException(SessionErrorCodes.HOSTING_SESSIONS_EXCEEDED)
            if (activeSessions.hasJoined(userId))
                throw BadRequestException(SessionErrorCodes.USER_ALREADY_JOINED_OTHER)
        }

        val sessionCode = SessionModel.generateSessionCode()

        return sessionService.createSession(
            CreateSessionRequest(
                command.autoplay?.acceptance,
                sessionCode,
                command.duration,
                userId,
                command.maxUsers
            )
        )
    }

    @Transactional
    fun joinSession(code: String, userId: UUID): SessionUserModel {
        val session = sessionService.findSessionByCode(code)

        if (session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.ALREADY_JOINED)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        if (!session.hasRoom())
            throw BadRequestException(SessionErrorCodes.MAX_USERS_EXCEEDED)

        sessionUserService.leaveActiveJoinedSessions(userId)

        return sessionUserService.createNew(userId, session)
    }

    @Transactional
    fun leaveSession(sessionId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        if (!session.hasJoined(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        sessionUserService.leaveSession(session, userId)
    }

    fun endSession(sessionId: UUID, userId: UUID) {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isHost(userId))
            throw BadRequestException(SessionErrorCodes.NOT_HOST)

        sessionService.endSession(sessionId)
    }

    fun current(userId: UUID): SessionModel {
        val session = sessionService.findByUser(userId) ?:
            throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND)

        return session
    }

    fun getSession(sessionId: UUID, userId: UUID): SessionModel {
        val session = sessionService.findSessionById(sessionId)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)
        if (!session.partOfSession(userId))
            throw BadRequestException(SessionErrorCodes.USER_NOT_JOINED)

        return session
    }
}