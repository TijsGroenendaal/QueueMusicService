package nl.tijsgroenendaal.idpservice.facades

import nl.tijsgroenendaal.idpservice.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.idpservice.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.idpservice.services.DeviceLinkService
import nl.tijsgroenendaal.idpservice.services.UserRefreshTokenService
import nl.tijsgroenendaal.idpservice.services.UserService
import nl.tijsgroenendaal.idpservice.services.UserLinkService
import nl.tijsgroenendaal.idpservice.security.JwtGenerator
import nl.tijsgroenendaal.idpservice.security.JwtGenerator.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.idpservice.security.JwtGenerator.Companion.JWT_TOKEN_VALIDITY
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.InvalidRefreshJwtException
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContext
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.qumusecurity.security.helper.getUserIdFromClaim
import nl.tijsgroenendaal.qumusecurity.security.model.QuMuAuthority
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicClaims

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Service
class AuthFacade(
		private val jwtTokenUtil: JwtTokenUtil,
		private val userService: UserService,
		private val userRefreshTokenService: UserRefreshTokenService,
		private val userLinkService: UserLinkService,
		private val deviceLinkService: DeviceLinkService,
		private val jwtGenerator: JwtGenerator
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val userId = userLinkService.login(code)

        val user = try {
            userService.findById(userId)
        } catch (e: BadRequestException) {
            userService.createUser(userId)
        }
        return createNewAccessTokens(user.id, true)
    }

    fun loginAnonymous(deviceId: String): LoginQueryResponse {
        val deviceLink = try {
            deviceLinkService.getByDeviceId(deviceId)
        } catch (e: BadRequestException) {
            val user = userService.createAnonymousUser()
            deviceLinkService.createDeviceLink(deviceId, user)
        }

        return createNewAccessTokens(deviceLink.user.id)
    }

    fun refresh(refreshToken: String): LoginQueryResponse {
        val userId = getUserIdFromClaim(getAuthenticationContext().claims.subject)
        val userModel = userService.findById(userId)

        if (userModel.userRefreshToken?.refreshToken != jwtTokenUtil.getTokenFromHeader(refreshToken))
            throw InvalidRefreshJwtException()

        return createNewAccessTokens(userModel.id, true)
    }

    fun logout() {
        userService.logout(getAuthenticationContextSubject())
        userLinkService.logout()
    }

    private fun createNewAccessTokens(userId: UUID, checkUserLink: Boolean = false): LoginQueryResponse {
        val userModel = userService.findById(userId)

        val userLink = if (checkUserLink) userLinkService.getByUserId(userModel.id) else null

        val authorities = mutableListOf<QuMuAuthority>()
        if (userLink != null) {
            authorities.add(QuMuAuthority("SPOTIFY"))
        }

        val userDetails = QueueMusicClaims(userId.toString())
            .apply {
                setScope(authorities)
                setUserId(userModel.id)
            }

        val refreshTokenString = jwtGenerator.generateToken(userDetails, JwtTypes.REFRESH)
        val expireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)

        if (userModel.userRefreshToken == null) {
            userModel.userRefreshToken = UserRefreshTokenModel(
                userModel,
                refreshTokenString,
                expireTime
            )
        } else {
            userModel.userRefreshToken!!.apply {
                this.refreshToken = refreshTokenString
                this.expireTime = expireTime
            }
        }

        userRefreshTokenService.save(userModel.userRefreshToken!!)

        val jwtToken = jwtGenerator.generateToken(userDetails, JwtTypes.ACCESS)

        return LoginQueryResponse(
            jwtToken,
            userModel.userRefreshToken!!.refreshToken,
            JWT_TOKEN_VALIDITY
        )
    }
}