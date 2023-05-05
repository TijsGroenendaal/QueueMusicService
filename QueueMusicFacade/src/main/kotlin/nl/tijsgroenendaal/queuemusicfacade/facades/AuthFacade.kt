package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.queuemusicfacade.helper.JwtTokenUtil
import nl.tijsgroenendaal.queuemusicfacade.helper.JwtTokenUtil.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.queuemusicfacade.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.queuemusicfacade.security.model.QueueMusicUserDetails
import nl.tijsgroenendaal.queuemusicfacade.entity.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicfacade.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.security.JwtTypes
import nl.tijsgroenendaal.queuemusicfacade.services.UserRefreshTokenService
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.qumu.exceptions.InvalidRefreshJwtException

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
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val accessToken = spotifyTokenClientService.getAccessToken(code)
        val linkUser = spotifyApiClientService.getMe(accessToken.accessToken)

        val user = userService.createUser(linkUser, accessToken)
        val userDetails = userService.findUserDetailsById(user.id)

        return createNewAccessTokens(userDetails)
    }

    fun loginAnonymous(deviceId: String): LoginQueryResponse {
        val user = userService.createAnonymousUser(deviceId)
        val userDetails = userService.findUserDetailsById(user.id)

        return createNewAccessTokens(userDetails)
    }

    fun refresh(refreshToken: String): LoginQueryResponse {
        val userDetails = userService.findUserDetailsById(getAuthenticationContextSubject())

        if (userDetails.userModel.userRefreshToken?.refreshToken != jwtTokenUtil.getTokenFromHeader(refreshToken))
            throw InvalidRefreshJwtException()

        return createNewAccessTokens(userDetails)
    }

    fun logout() {
        val user = userService
            .findById(getAuthenticationContextSubject())
            .apply {
                this.userRefreshToken = null
                this.userLink?.linkAccessToken = null
                this.userLink?.linkRefreshToken = null
            }

        userService.save(user)
    }

    private fun createNewAccessTokens(userDetails: QueueMusicUserDetails): LoginQueryResponse {
        userDetails.apply {
            if (this.userModel.userRefreshToken == null) {
                this.userModel.userRefreshToken = UserRefreshTokenModel(
                    this.userModel,
                    jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH),
                    LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)
                )
            } else {
                this.userModel.userRefreshToken?.apply {
                    this.refreshToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH)
                    this.expireTime = LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY)
                }
            }
        }

        userRefreshTokenService.save(userDetails.userModel.userRefreshToken!!)

        val jwtToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.ACCESS)

        return LoginQueryResponse(
            jwtToken,
            userDetails.userModel.userRefreshToken!!.refreshToken
        )
    }
}