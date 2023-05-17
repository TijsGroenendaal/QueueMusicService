package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyTokenClientService
import nl.tijsgroenendaal.spotifyfacade.entity.UserLinkModel
import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService

import org.springframework.stereotype.Service

@Service
class AuthFacade(
    private val userLinkService: UserLinkService,
    private val spotifyApiClientService: SpotifyApiClientService,
    private val spotifyTokenClientService: SpotifyTokenClientService
) {

    fun login(code: String): UserLinkModel {
        val accessToken = spotifyTokenClientService.getAccessToken(code)
        val linkUser = spotifyApiClientService.getMe(accessToken.accessToken)

        return try {
            userLinkService.findByLinkId(linkUser.id)
        } catch (e: BadRequestException) {
            userLinkService.create(accessToken.accessToken, accessToken.refreshToken, accessToken.expiresIn, linkUser.id)
        }
    }

    fun logout() {
        userLinkService.logout()
    }

}