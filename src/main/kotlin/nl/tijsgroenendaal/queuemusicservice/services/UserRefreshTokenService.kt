package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.repositories.UserRefreshTokenRepository

import org.springframework.stereotype.Service

@Service
class UserRefreshTokenService(
    private val userRefreshTokenRepository: UserRefreshTokenRepository
) {

}