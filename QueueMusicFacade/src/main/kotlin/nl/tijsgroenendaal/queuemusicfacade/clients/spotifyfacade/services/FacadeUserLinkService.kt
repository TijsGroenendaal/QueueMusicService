package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.SpotifyFacadeClient
import org.springframework.stereotype.Service

@Service
class FacadeUserLinkService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun logout() {
        spotifyFacadeClient.logout()
    }

}