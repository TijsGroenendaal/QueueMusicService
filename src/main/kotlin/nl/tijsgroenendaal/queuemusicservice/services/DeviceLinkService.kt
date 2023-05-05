package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicservice.repositories.UserDeviceLinkRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.DeviceLinkErrorCodes

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class DeviceLinkService(
    private val userDeviceLinkRepository: UserDeviceLinkRepository
) {

    fun getByUserId(userId: UUID): UserDeviceLinkModel {
        return userDeviceLinkRepository.findByUserModelId(userId)
            ?: throw BadRequestException(DeviceLinkErrorCodes.DEVICE_LINK_NOT_AVAILABLE, "User $userId has no deviceLink defined")
    }

}