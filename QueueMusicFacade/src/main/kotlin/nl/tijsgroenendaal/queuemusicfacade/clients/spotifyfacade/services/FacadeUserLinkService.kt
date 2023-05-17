package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.AnonymousSpotifyFacadeClient
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetUserLinkByUserIdQueryResponse

import feign.FeignException

import org.springframework.stereotype.Service

import java.util.UUID

// TODO rename to UserLinkFacade once UserLinkService is gone
@Service
class FacadeUserLinkService(
    private val spotifyFacadeClient: SpotifyFacadeClient,
    private val anonymousSpotifyFacadeClient: AnonymousSpotifyFacadeClient
) {

    fun logout() {
        spotifyFacadeClient.logout()
    }

    fun login(code: String): UUID {
        return anonymousSpotifyFacadeClient.login(code)
    }

    fun getByUserId(userId: UUID): GetUserLinkByUserIdQueryResponse? {
        return try {
            spotifyFacadeClient.getByUserId(userId)
        } catch (e: FeignException) {
            null
        }
    }

}