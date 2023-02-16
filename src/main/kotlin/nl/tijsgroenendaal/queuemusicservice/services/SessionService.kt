package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSession
import nl.tijsgroenendaal.queuemusicservice.exceptions.BadRequestException
import nl.tijsgroenendaal.queuemusicservice.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.queuemusicservice.repositories.QueueMusicSessionRepository
import nl.tijsgroenendaal.queuemusicservice.services.commands.CreateSessionCommand

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: QueueMusicSessionRepository
) {

    fun getActiveSessionsByUser(userId: UUID): List<QueueMusicSession> {
        val now = LocalDateTime.now(ZoneOffset.UTC)
        return sessionRepository.findAllByHostIdAndEndAtAfterAndManualEnded(userId, now)
    }

    fun createSession(command: CreateSessionCommand): QueueMusicSession {
        return sessionRepository.save(QueueMusicSession.new(command))
    }

    fun findSessionById(sessionId: UUID): QueueMusicSession {
        return sessionRepository.findById(sessionId).let {
            if (it.isEmpty) throw BadRequestException(SessionErrorCodes.SESSION_NOT_FOUND, "Session $sessionId not found")
            else it.get()
        }
    }
}