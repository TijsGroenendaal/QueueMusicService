package nl.tijsgroenendaal.idpservice.repositories

import nl.tijsgroenendaal.idpservice.entity.UserDeviceLinkModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserDeviceLinkRepository: JpaRepository<UserDeviceLinkModel, UUID> {
    fun findByDeviceId(deviceId: String): UserDeviceLinkModel?
}