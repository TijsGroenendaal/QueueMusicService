package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.commands.reponses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.qumu.helper.BadRequestSerializer

import java.util.UUID

import feign.FeignException

import org.springframework.stereotype.Service

@Service
class SpotifyService(
    private val spotifyFacadeClient: SpotifyFacadeClient
) {

    fun getTrack(trackId: String): GetTrackQueryResponse {
        return try {
            spotifyFacadeClient.getTrack(trackId)
        } catch (e: FeignException) {
            throw BadRequestSerializer.getBadRequestException(e)
        }
    }

    fun createPlaylist(name: String, userId: UUID): CreatePlaylistCommandResponse {
        return try {
            spotifyFacadeClient.createPlaylist(name, userId)
        } catch (e: FeignException) {
            throw BadRequestSerializer.getBadRequestException(e)
        }
    }
}