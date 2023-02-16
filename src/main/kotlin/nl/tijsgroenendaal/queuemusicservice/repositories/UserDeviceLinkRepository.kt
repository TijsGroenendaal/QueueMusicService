package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.entity.UserDeviceLink

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserDeviceLinkRepository: JpaRepository<UserDeviceLink, UUID> {
    fun findByUserModelId(userModelId: UUID): UserDeviceLink?
}