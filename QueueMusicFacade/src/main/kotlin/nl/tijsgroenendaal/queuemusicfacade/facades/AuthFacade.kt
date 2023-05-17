package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.FacadeUserLinkService
import nl.tijsgroenendaal.queuemusicfacade.entity.UserModel
import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicfacade.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.services.UserRefreshTokenService
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.qumu.exceptions.InvalidRefreshJwtException
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class AuthFacade(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userService: UserService,
    private val userRefreshTokenService: UserRefreshTokenService,
    private val spotifyTokenClientService: SpotifyTokenClientService,
    private val spotifyApiClientService: SpotifyApiClientService,
    private val facadeUserLinkService: FacadeUserLinkService
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val accessToken = spotifyTokenClientService.getAccessToken(code)
        val linkUser = spotifyApiClientService.getMe(accessToken.accessToken)

        val user = userService.createUser(linkUser, accessToken)
        val userModel = userService.findById(user.id)

        return createNewAccessTokens(userModel)
    }

    fun loginAnonymous(deviceId: String): LoginQueryResponse {
        val userModel = userService.createAnonymousUser(deviceId)

        return createNewAccessTokens(userModel)
    }

    fun refresh(refreshToken: String): LoginQueryResponse {
        val userModel = userService.findById(getAuthenticationContextSubject())

        if (userModel.userRefreshToken?.refreshToken != jwtTokenUtil.getTokenFromHeader(refreshToken))
            throw InvalidRefreshJwtException()

        return createNewAccessTokens(userModel)
    }

    fun logout() {
        userService.logout(getAuthenticationContextSubject())
        facadeUserLinkService.logout()
    }

    private fun createNewAccessTokens(userModel: UserModel): LoginQueryResponse {
        val userDetails = userService.findUserDetailsById(userModel.id)

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