package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.entity.UserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserModel, UUID> {

    fun findByUserLinkLinkId(userLinkLinkId: String): UserModel?
    fun findByUserDeviceLinkDeviceId(userDeviceLinkDeviceId: String): UserModel?
}