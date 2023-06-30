package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserDeviceLinkRepository: JpaRepository<UserDeviceLinkModel, UUID> {
    fun findByUserModelId(userModelId: UUID): UserDeviceLinkModel?
    fun findByDeviceId(deviceId: String): UserDeviceLinkModel?

    fun deleteByUserModel(userModel: UserModel)
}