package nl.tijsgroenendaal.sessionservice.session

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionErrorCodes
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: SessionRepository
) {

    fun getActiveSessionsByUser(userId: UUID): List<SessionModel> {
        return sessionRepository.findAllByHostAndEndAtAfterAndManualEnded(userId)
    }

    fun createSession(command: CreateSessionRequest): SessionModel {
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

    fun findByUser(userId: UUID): SessionModel? {
        val joined = sessionRepository.findByUser(userId)
        val hosting = sessionRepository.findByHost(userId)

        return hosting ?: joined
    }
}