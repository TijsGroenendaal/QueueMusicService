package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicfacade.repositories.UserRefreshTokenRepository

import org.springframework.stereotype.Service

@Service
class UserRefreshTokenService(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

    fun save(userRefreshTokenModel: UserRefreshTokenModel): UserRefreshTokenModel =
        userRefreshTokenRepository.save(userRefreshTokenModel)

}