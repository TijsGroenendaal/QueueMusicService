package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.UserDeviceLinkRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.DeviceLinkErrorCodes

import org.springframework.stereotype.Service

@Service
class DeviceLinkService(
    private val userDeviceLinkRepository: UserDeviceLinkRepository
) {

    fun getByDeviceId(deviceId: String): UserDeviceLinkModel {
        return userDeviceLinkRepository.findByDeviceId(deviceId)
            ?: throw BadRequestException(DeviceLinkErrorCodes.DEVICE_LINK_NOT_FOUND)
    }

    fun createDeviceLink(deviceId: String, user: UserModel): UserDeviceLinkModel =
        userDeviceLinkRepository.save(UserDeviceLinkModel.new(deviceId, user))
}