package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.facade

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.track.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyApiClientService
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.services.SpotifyTokenClientService

import org.springframework.stereotype.Service

@Service
class SpotifyFacade(
    private val spotifyTokenClientService: SpotifyTokenClientService,
    private val spotifyApiClientService: SpotifyApiClientService
) {

    fun getTrack(songId: String): GetTrackQueryResponse {
            return spotifyApiClientService.getTrack(songId, spotifyTokenClientService.getCredentialsAccessToken().accessToken)
    }
}