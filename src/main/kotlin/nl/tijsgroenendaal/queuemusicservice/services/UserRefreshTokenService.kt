package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicservice.repositories.UserRefreshTokenRepository

import org.springframework.stereotype.Service

@Service
class UserRefreshTokenService(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

    fun save(userRefreshTokenModel: UserRefreshTokenModel): UserRefreshTokenModel =
        userRefreshTokenRepository.save(userRefreshTokenModel)

}