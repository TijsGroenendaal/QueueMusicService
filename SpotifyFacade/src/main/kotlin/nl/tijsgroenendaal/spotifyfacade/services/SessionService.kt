package nl.tijsgroenendaal.spotifyfacade.services

import nl.tijsgroenendaal.spotifyfacade.exceptions.BadRequestException
import nl.tijsgroenendaal.spotifyfacade.repositories.SessionRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SessionService(
    private val sessionRepository: SessionRepository
) {

    fun getPlaylistIdBySessionId(sessionId: UUID): String {
        return sessionRepository.findPlaylistIdBySessionId(sessionId)
            ?: throw BadRequestException(HttpStatus.NOT_FOUND.value().toString(), "Session not found with id $sessionId")
    }

}