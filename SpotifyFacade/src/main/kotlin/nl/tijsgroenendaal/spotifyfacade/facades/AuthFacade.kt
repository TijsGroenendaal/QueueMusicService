package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.UserLinkErrorCodes
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

    fun login(code: String, redirectUri: String): UserLinkModel {
        val accessToken = spotifyTokenClientService.getAccessToken(code, redirectUri)
        val linkUser = spotifyApiClientService.getMe(accessToken.accessToken)

        return try {
            val userLink = userLinkService.findByLinkId(linkUser.id)
            userLinkService.update(userLink, accessToken)

            return userLink
        } catch (e: BadRequestException) {
            if (e.isError(UserLinkErrorCodes.USER_LINK_NOT_FOUND)) {
                userLinkService.create(accessToken.accessToken, accessToken.refreshToken, accessToken.expiresIn, linkUser.id)
            } else {
                throw e
            }
        }
    }

    fun logout() {
        userLinkService.logout()
    }

}