package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLink
import nl.tijsgroenendaal.queuemusicservice.exceptions.BadRequestException
import nl.tijsgroenendaal.queuemusicservice.exceptions.DeviceLinkErrorCodes
import nl.tijsgroenendaal.queuemusicservice.repositories.UserDeviceLinkRepository

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class DeviceLinkService(
    private val userDeviceLinkRepository: UserDeviceLinkRepository
) {

    fun getByUserId(userId: UUID): UserDeviceLink {
        return userDeviceLinkRepository.findByUserModelId(userId)
            ?: throw BadRequestException(DeviceLinkErrorCodes.DEVICE_LINK_NOT_AVAILABLE, "User $userId has no deviceLink defined")
    }

}