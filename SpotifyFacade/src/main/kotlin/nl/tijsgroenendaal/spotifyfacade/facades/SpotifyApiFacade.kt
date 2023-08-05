package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.spotifyfacade.commands.AddPlaylistTrackCommand
import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SpotifyApiFacade(
    private val userLinkService: UserLinkService,
    private val spotifyApiClientService: SpotifyApiClientService,
    private val userLinkFacade: UserLinkFacade
) {

    fun getTrack(trackId: String): GetTrackQueryResponse {
        return spotifyApiClientService.getTrack(trackId)
    }

    fun createPlaylist(name: String, userId: UUID): CreatePlaylistCommandResponse {
        val userLink = userLinkService.findByUserId(userId)
        val accessToken = userLinkFacade.getAccessToken(userId)

        return spotifyApiClientService.createPlaylist(userLink.linkId, name, accessToken)
    }

    fun addPlaylistTrack(command: AddPlaylistTrackCommand) {
        val accessToken = userLinkFacade.getAccessToken(command.userId)

        spotifyApiClientService.addPlaylistTrack(command.playlistId, command.trackId, command.position, accessToken)
    }
}