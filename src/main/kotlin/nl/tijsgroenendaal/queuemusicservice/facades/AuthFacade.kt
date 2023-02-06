package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil
import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil.Companion.JWT_REFRESH_TOKEN_VALIDITY
import nl.tijsgroenendaal.queuemusicservice.models.UserRefreshTokenModel
import nl.tijsgroenendaal.queuemusicservice.query.responses.LoginQueryResponse
import nl.tijsgroenendaal.queuemusicservice.security.JwtTypes
import nl.tijsgroenendaal.queuemusicservice.services.UserService

import org.springframework.stereotype.Service

import java.time.LocalDateTime
import java.time.ZoneOffset

@Service
class AuthFacade(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userService: UserService,
    private val spotifyTokenClientService: SpotifyTokenClientService,
    private val spotifyApiClientService: SpotifyApiClientService,
    private val jwtUserDetailsFacade: JwtUserDetailsFacade
) {

    fun loginLinkUser(code: String): LoginQueryResponse {
        val accessToken = spotifyTokenClientService.getAccessToken(code)
        val linkUser = spotifyApiClientService.getMe(accessToken.accessToken)

        val user = userService.createUser(linkUser, accessToken)

        val userDetails = jwtUserDetailsFacade.loadUserByUsername(user.id.toString())

        user.apply {
            this.userRefreshToken = UserRefreshTokenModel(
                this,
                jwtTokenUtil.generateToken(userDetails, JwtTypes.REFRESH),
                LocalDateTime.now(ZoneOffset.UTC).plusSeconds(JWT_REFRESH_TOKEN_VALIDITY.toLong())
            )
        }

        userService.save(user)

        val jwtToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.ACCESS)

        return LoginQueryResponse(
            jwtToken,
            user.userRefreshToken?.refreshToken!!
        )
    }
}