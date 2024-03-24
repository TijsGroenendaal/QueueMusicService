package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services

import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.SessionSongErrorCode
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyApiClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyOpenClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.tracks.GetTrackQueryResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse

import feign.FeignException

import org.springframework.stereotype.Service

@Service
class SpotifyApiClientService(
    private val spotifyApiClient: SpotifyApiClient,
    private val spotifyOpenClient: SpotifyOpenClient
) {

    fun getMe(accessCode: String): GetMeQueryResponse {
        return spotifyApiClient.getMe("Bearer $accessCode")
    }

    fun getTrack(trackId: String): GetTrackQueryResponse {
        return try {
            spotifyOpenClient.getTrack(trackId)
        } catch (e: FeignException) {
            if (e.status() == 404 || e.status() == 400) {
                throw BadRequestException(SessionSongErrorCode.TRACK_NOT_FOUND)
            } else {
                throw e
            }
        }
    }

    fun queueTrack(trackId: String, accessToken: String) {
        spotifyApiClient.queueTrack(
            "spotify:track:$trackId",
            "Bearer $accessToken")
    }
}