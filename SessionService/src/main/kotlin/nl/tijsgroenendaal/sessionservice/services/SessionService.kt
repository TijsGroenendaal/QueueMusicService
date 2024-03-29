package nl.tijsgroenendaal.sessionservice.services

import nl.tijsgroenendaal.sessionservice.entity.SessionModel
import nl.tijsgroenendaal.sessionservice.repositories.QueueMusicSessionRepository
import nl.tijsgroenendaal.sessionservice.services.commands.CreateSessionCommand
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: QueueMusicSessionRepository
) {

    fun getActiveSessionsByUser(userId: UUID): List<SessionModel> {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        return sessionRepository.findAllByHostAndEndAtAfterAndManualEnded(userId, now)
    }

    fun createSession(command: CreateSessionCommand): SessionModel {
        return sessionRepository.save(SessionModel.new(command))
    }

    fun findSessionById(sessionId: UUID): SessionModel {
        return sessionRepository.findById(sessionId).let {
            if (it.isEmpty) throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND)
            else it.get()
        }
    }

    fun findSessionByCode(code: String): SessionModel {
        return sessionRepository.findByCode(code)
            ?: throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND)
    }

    fun endSession(sessionId: UUID): SessionModel {
        val session = this.findSessionById(sessionId)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        session.end()

        return sessionRepository.save(session)
    }
}