package nl.tijsgroenendaal.idpservice.services

import nl.tijsgroenendaal.idpservice.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.idpservice.repositories.UserRefreshTokenRepository

import org.springframework.stereotype.Service

@Service
class UserRefreshTokenService(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

    fun save(userRefreshTokenModel: UserRefreshTokenModel): UserRefreshTokenModel =
        userRefreshTokenRepository.save(userRefreshTokenModel)

}