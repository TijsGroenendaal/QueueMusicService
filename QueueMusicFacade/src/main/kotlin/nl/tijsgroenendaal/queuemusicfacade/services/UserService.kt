package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.UserRepository
import nl.tijsgroenendaal.queuemusicfacade.entity.UserDeviceLinkModel
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.UserErrorCodes

import org.springframework.stereotype.Service

import jakarta.transaction.Transactional

import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    fun findById(id: UUID): UserModel {
        return userRepository
            .findById(id)
            .let {
                if(it.isEmpty) throw BadRequestException(UserErrorCodes.USER_NOT_FOUND, "User $id not found")
                it.get()
            }
    }

    @Transactional
    fun createUser(
        userId: UUID,
    ): UserModel {
        val persistentUser = userRepository.findById(userId)
        if (persistentUser.isEmpty) return userRepository.save(UserModel(userId))

        return persistentUser.get()
    }

    fun createAnonymousUser(deviceId: String): UserModel {
        val persistentUser = userRepository
            .findByUserDeviceLinkDeviceId(deviceId)
            .let {
                it ?: UserModel(UUID.randomUUID()).apply {
                    this.userDeviceLink = UserDeviceLinkModel.new(deviceId, this)
                }
            }

        return userRepository.save(persistentUser)
    }

    fun logout(userId: UUID) {
        val user = findById(userId).apply {
            this.userRefreshToken = null
        }

        save(user)
    }

    @Transactional
    fun save(userModel: UserModel): UserModel = userRepository.save(userModel)
}