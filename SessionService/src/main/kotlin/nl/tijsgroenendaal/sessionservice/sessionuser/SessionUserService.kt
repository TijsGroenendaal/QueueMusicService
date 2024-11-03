package nl.tijsgroenendaal.sessionservice.sessionuser

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserModel
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserRepository

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class SessionUserService(
    private val sessionUserRepository: SessionUserRepository
) {

    fun createNew(userId: UUID, session: SessionModel): SessionUserModel =
        sessionUserRepository.save(SessionUserModel.new(userId, session))

    fun leaveSession(session: SessionModel, user: UUID) {
        sessionUserRepository.delete(session.sessionUsers.first { it.user == user })
    }

    fun leaveActiveJoinedSessions(user: UUID) {
        sessionUserRepository.deleteAllByUserAndSessionEndAtAfterAndSessionManualEnded(user)
    }

}