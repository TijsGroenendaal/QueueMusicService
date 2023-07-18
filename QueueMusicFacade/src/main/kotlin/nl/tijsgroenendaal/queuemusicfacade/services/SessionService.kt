package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.QueueMusicSessionRepository
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand
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
        return sessionRepository.findAllByHostIdAndEndAtAfterAndManualEnded(userId, now)
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

    fun endSession(code: String): SessionModel {
        val session = this.findSessionByCode(code)

        if (!session.isActive())
            throw BadRequestException(SessionErrorCodes.SESSION_ENDED)

        session.end()

        return sessionRepository.save(session)
    }
}