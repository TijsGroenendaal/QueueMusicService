package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.commands.reponses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.qumu.helper.catchingFeignRequest

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SpotifyService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun getTrack(trackId: String): GetTrackQueryResponse {
        return catchingFeignRequest { spotifyFacadeClient.getTrack(trackId) }
    }

    fun createPlaylist(name: String, userId: UUID): CreatePlaylistCommandResponse {
        return catchingFeignRequest { spotifyFacadeClient.createPlaylist(name, userId) }
    }
}