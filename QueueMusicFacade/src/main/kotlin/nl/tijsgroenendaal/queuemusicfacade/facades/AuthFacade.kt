package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.UserLinkService
import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicfacade.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.services.UserRefreshTokenService
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.qumu.exceptions.InvalidRefreshJwtException
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.qumusecurity.security.model.Authorities
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicPrincipalAuthentication
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicUserDetails

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class AuthFacade(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userService: UserService,
    private val userRefreshTokenService: UserRefreshTokenService,
    private val userLinkService: UserLinkService
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val userId = userLinkService.login(code)

        val user = userService.createUser(userId)
        val userModel = userService.findById(user.id)

        return createNewAccessTokens(userModel.id)
    }

    fun loginAnonymous(deviceId: String): LoginQueryResponse {
        val userModel = userService.createAnonymousUser(deviceId)

        return createNewAccessTokens(userModel.id)
    }

    fun refresh(refreshToken: String): LoginQueryResponse {
        val userModel = userService.findById(getAuthenticationContextSubject())

        if (userModel.userRefreshToken?.refreshToken != jwtTokenUtil.getTokenFromHeader(refreshToken))
            throw InvalidRefreshJwtException()

        return createNewAccessTokens(userModel.id)
    }

    fun logout() {
        userService.logout(getAuthenticationContextSubject())
        userLinkService.logout()
    }

    private fun createNewAccessTokens(userId: UUID): LoginQueryResponse {
        val userModel = userService.findById(userId)
        SecurityContextHolder.getContext().authentication = QueueMusicAuthentication(
            QueueMusicPrincipalAuthentication(
                userModel.id,
                null,
                emptySet()
            ),
            emptySet()
        )

        val userLink = userLinkService.getByUserId(userModel.id)

        val authorities = mutableListOf(Authorities.REFRESH)
        if (userLink != null) {
            authorities.add(Authorities.SPOTIFY)
        }

        val userDetails = QueueMusicUserDetails(userModel.id, userModel.userDeviceLink?.deviceId, authorities.toSet())

        if (userModel.userRefreshToken == null) {
            userModel.userRefreshToken = UserRefreshTokenModel(
                userModel,
                jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH),
                LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)
            )
        } else {
            userModel.userRefreshToken?.apply {
                this.refreshToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH)
                this.expireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)
            }
        }

        userRefreshTokenService.save(userModel.userRefreshToken!!)

        val jwtToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.ACCESS)

        return LoginQueryResponse(
            jwtToken,
            userModel.userRefreshToken!!.refreshToken
        )
    }
}