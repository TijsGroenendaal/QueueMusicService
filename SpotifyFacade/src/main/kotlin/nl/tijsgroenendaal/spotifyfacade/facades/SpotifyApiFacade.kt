package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.spotifyfacade.commands.QueueTrackCommand

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SpotifyApiFacade(
    private val spotifyApiClientService: SpotifyApiClientService,
    private val userLinkFacade: UserLinkFacade
) {

    fun getTrack(trackId: String): GetTrackQueryResponse {
        return spotifyApiClientService.getTrack(trackId)
    }

    fun queueTrack(command: QueueTrackCommand) {
        val accessToken = userLinkFacade.getAccessToken(command.hostId)

        spotifyApiClientService.queueTrack(command.trackId, accessToken)
    }

    fun getSpotifyUserById(userId: UUID): GetMeQueryResponse {
        val accessToken = userLinkFacade.getAccessToken(userId)

        return spotifyApiClientService.getMe(accessToken)
    }
}