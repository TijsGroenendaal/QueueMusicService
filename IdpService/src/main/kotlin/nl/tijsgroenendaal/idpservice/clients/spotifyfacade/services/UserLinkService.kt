package nl.tijsgroenendaal.idpservice.clients.spotifyfacade.services

import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients.AnonymousSpotifyFacadeClient
import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.idpservice.clients.spotifyfacade.query.responses.GetUserLinkByUserIdQueryResponse
import nl.tijsgroenendaal.qumu.exceptions.UserLinkErrorCodes
import nl.tijsgroenendaal.qumu.helper.BadRequestSerializer

import feign.FeignException

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

    fun login(code: String): UUID {
        return try {
            anonymousSpotifyFacadeClient.login(code)
        } catch (e: FeignException) {
            throw BadRequestSerializer.getBadRequestException(e)
        }
    }

    fun getByUserId(userId: UUID): GetUserLinkByUserIdQueryResponse? {
        return try {
            spotifyFacadeClient.getByUserId(userId)
        } catch (e: FeignException) {
            val badRequestException = BadRequestSerializer.getBadRequestException(e)
            if (badRequestException.isError(UserLinkErrorCodes.USER_LINK_NOT_FOUND)) {
                null
            } else {
                throw badRequestException
            }
        }
    }
}