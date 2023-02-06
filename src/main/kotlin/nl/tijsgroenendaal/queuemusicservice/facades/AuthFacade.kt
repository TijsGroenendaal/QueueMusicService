package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil
import nl.tijsgroenendaal.queuemusicservice.services.UserService
import org.springframework.stereotype.Service

@Service
class AuthFacade(
    private val jwtTokenUtil: JwtTokenUtil,
    private val userService: UserService,
    private val spotifyTokenClientService: SpotifyTokenClientService
) {

    fun linkToUser(code: String) {

    }

}