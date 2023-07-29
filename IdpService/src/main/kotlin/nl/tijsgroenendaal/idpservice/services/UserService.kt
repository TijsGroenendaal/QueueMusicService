package nl.tijsgroenendaal.idpservice.services

import nl.tijsgroenendaal.idpservice.entity.UserModel
import nl.tijsgroenendaal.idpservice.repositories.UserRepository
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
                if(it.isEmpty) throw BadRequestException(UserErrorCodes.USER_NOT_FOUND)
                it.get()
            }
    }

    fun createUser(userId: UUID): UserModel {
        return userRepository.save(UserModel.new(userId))
    }

    fun createAnonymousUser(): UserModel {
        return userRepository.save(UserModel.new())
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