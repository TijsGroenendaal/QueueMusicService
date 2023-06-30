package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserDeviceLinkRepository: JpaRepository<UserDeviceLinkModel, UUID> {
    fun findByUserId(userModelId: UUID): UserDeviceLinkModel?
    fun findByDeviceId(deviceId: String): UserDeviceLinkModel?
}