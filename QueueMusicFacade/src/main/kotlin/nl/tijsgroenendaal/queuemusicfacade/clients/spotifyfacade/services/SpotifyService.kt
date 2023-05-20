package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.commands.reponses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses.GetTrackQueryResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.clients.SpotifyFacadeClient
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode

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
            if (e.status() == 404 || e.status() == 400) {
                throw BadRequestException(SessionSongErrorCode.TRACK_NOT_FOUND, "Track $trackId not found")
            } else {
                throw e
            }
        }
    }

    fun createPlaylist(name: String): CreatePlaylistCommandResponse =
        spotifyFacadeClient.createPlaylist(name)
}