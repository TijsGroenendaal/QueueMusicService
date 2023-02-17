package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSessionModel
import nl.tijsgroenendaal.queuemusicservice.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicservice.repositories.SessionUserRepository

import org.springframework.stereotype.Service

@Service
class SessionUserService(
    private val sessionUserRepository: SessionUserRepository
) {

    fun createNew(deviceLink: UserDeviceLinkModel, session: QueueMusicSessionModel): SessionUserModel =
        sessionUserRepository.save(SessionUserModel.new(deviceLink, session))

}