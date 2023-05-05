package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.QueueMusicSessionModel
import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.SessionUserRepository

import org.springframework.stereotype.Service

@Service
class SessionUserService(
    private val sessionUserRepository: SessionUserRepository
) {

    fun createNew(deviceLink: UserDeviceLinkModel, session: QueueMusicSessionModel): SessionUserModel =
        sessionUserRepository.save(SessionUserModel.new(deviceLink, session))

}