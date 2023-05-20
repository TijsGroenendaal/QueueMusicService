package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService

import org.springframework.stereotype.Service

@Service
class SpotifyApiFacade(
    private val userLinkService: UserLinkService,
    private val spotifyApiClientService: SpotifyApiClientService
) {

    fun createPlaylist(name: String): CreatePlaylistCommandResponse {
        val userLink = userLinkService.findByUserId(getAuthenticationContextSubject())

        return spotifyApiClientService.createPlaylist(userLink.linkId, name)
    }
}