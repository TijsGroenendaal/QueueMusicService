package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.SessionUserRepository

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class SessionUserService(
    private val sessionUserRepository: SessionUserRepository
) {

    fun createNew(user: UserModel, session: SessionModel): SessionUserModel =
        sessionUserRepository.save(SessionUserModel.new(user, session))

    fun leaveSession(session: SessionModel, user: UUID) {
        sessionUserRepository.delete(session.sessionUsers.first { it.user.id == user })
    }

    fun leaveActiveJoinedSessions(user: UUID) {
        sessionUserRepository.deleteAllByUserIdAndSessionEndAtAfterAndSessionManualEnded(user)
    }

}