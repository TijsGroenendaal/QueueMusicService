package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.QueueMusicSessionModel
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

    fun getActiveSessionsByUser(userId: UUID): List<QueueMusicSessionModel> {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        return sessionRepository.findAllByHostIdAndEndAtAfterAndManualEnded(userId, now)
    }

    fun createSession(command: CreateSessionCommand): QueueMusicSessionModel {
        return sessionRepository.save(QueueMusicSessionModel.new(command))
    }

    fun findSessionById(sessionId: UUID): QueueMusicSessionModel {
        return sessionRepository.findById(sessionId).let {
            if (it.isEmpty) throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND)
            else it.get()
        }
    }

    fun findSessionByCode(code: String): QueueMusicSessionModel {
        return sessionRepository.findByCode(code)
            ?: throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND)
    }
}