package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.SessionUserRepository

import org.springframework.stereotype.Service

import jakarta.transaction.Transactional

import java.util.UUID

@Service
class SessionUserService(
    private val sessionUserRepository: SessionUserRepository
) {

    fun createNew(user: UserModel, session: SessionModel): SessionUserModel =
        sessionUserRepository.save(SessionUserModel.new(user, session))

    @Transactional
    fun leaveSession(code: String, user: UUID) = sessionUserRepository.deleteByUserIdAndSessionCode(user, code)
}