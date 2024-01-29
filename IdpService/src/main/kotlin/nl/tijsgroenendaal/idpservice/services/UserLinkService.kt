package nl.tijsgroenendaal.idpservice.services

import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients.AnonymousSpotifyFacadeClient
import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.query.responses.GetUserLinkByUserIdQueryResponse
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.UserLinkErrorCodes
import nl.tijsgroenendaal.qumu.helper.catchingFeignRequest

import org.springframework.stereotype.Service

import java.util.UUID

@Service
class UserLinkService(
    private val spotifyFacadeClient: SpotifyFacadeClient,
    private val anonymousSpotifyFacadeClient: AnonymousSpotifyFacadeClient
) {

    fun logout() {
        spotifyFacadeClient.logout()
    }

    fun login(code: String, redirectUri: String): UUID = catchingFeignRequest { anonymousSpotifyFacadeClient.login(code, redirectUri) }

    fun getByUserId(userId: UUID): GetUserLinkByUserIdQueryResponse? {
        return try {
            catchingFeignRequest { spotifyFacadeClient.getByUserId(userId) }
        } catch (e: BadRequestException) {
            if (e.isError(UserLinkErrorCodes.USER_LINK_NOT_FOUND)) null
            else throw e
        }
    }
}