package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.UserDeviceLinkRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.DeviceLinkErrorCodes

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class DeviceLinkService(
    private val userDeviceLinkRepository: UserDeviceLinkRepository
) {

    fun getByDeviceId(deviceId: String): UserDeviceLinkModel {
        return userDeviceLinkRepository.findByDeviceId(deviceId)
            ?: throw BadRequestException(DeviceLinkErrorCodes.DEVICE_LINK_NOT_FOUND)
    }

    fun getByUserId(userId: UUID): UserDeviceLinkModel {
        return userDeviceLinkRepository.findByUserId(userId)
            ?: throw BadRequestException(DeviceLinkErrorCodes.DEVICE_LINK_NOT_FOUND)
    }

    fun attachToUser(deviceId: String, user: UserModel): UserDeviceLinkModel {
        val existingDeviceIdLink = try { getByDeviceId(deviceId) } catch (e: BadRequestException) { null }
        if (existingDeviceIdLink != null) {
            if (existingDeviceIdLink.user.id == user.id) {
                return existingDeviceIdLink
            }
            deleteById(existingDeviceIdLink.id)
        }

        val existingUserDeviceLink = try { getByUserId(user.id) } catch (e: BadRequestException) { null }
        if (existingUserDeviceLink != null) {
            existingUserDeviceLink.deviceId = deviceId
            return userDeviceLinkRepository.save(existingUserDeviceLink)
        }

        return userDeviceLinkRepository.save(UserDeviceLinkModel.new(deviceId, user))
    }

    private fun deleteById(deviceLinkId: UUID) {
        userDeviceLinkRepository.deleteById(deviceLinkId)
    }

}