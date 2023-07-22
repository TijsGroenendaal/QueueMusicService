package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.UserLinkService
import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicfacade.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.services.DeviceLinkService
import nl.tijsgroenendaal.queuemusicfacade.services.UserRefreshTokenService
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.InvalidRefreshJwtException
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
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
    private val deviceLinkService: DeviceLinkService
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val userId = userLinkService.login(code)

        val user = try {
            userService.findById(userId)
        } catch (e: BadRequestException) {
            userService.createUser(userId)
        }
        return createNewAccessTokens(user.id)
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
        val userModel = userService.findById(getAuthenticationContextSubject())

        if (userModel.userRefreshToken?.refreshToken != jwtTokenUtil.getTokenFromHeader(refreshToken))
            throw InvalidRefreshJwtException()

        return createNewAccessTokens(userModel.id)
    }

    fun logout() {
        userService.logout(getAuthenticationContextSubject())
        userLinkService.logout()
    }

    private fun createNewAccessTokens(userId: UUID, checkUserLink: Boolean = false): LoginQueryResponse {
        val userModel = userService.findById(userId)

        val userLink = if (checkUserLink) userLinkService.getByUserId(userModel.id) else null

        val authorities = mutableListOf(QuMuAuthority("REFRESH"))
        if (userLink != null) {
            authorities.add(QuMuAuthority("SPOTIFY"))
        }

        val userDetails = QueueMusicClaims(userId.toString())
            .apply {
                setScope(authorities)
                setUserId(userModel.id)
            }

        if (userModel.userRefreshToken == null) {
            userModel.userRefreshToken = UserRefreshTokenModel(
                userModel,
                jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH),
                LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)
            )
        } else {
            userModel.userRefreshToken!!.apply {
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