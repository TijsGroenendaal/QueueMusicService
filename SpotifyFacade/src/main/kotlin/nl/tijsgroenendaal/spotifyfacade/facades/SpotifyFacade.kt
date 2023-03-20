package nl.tijsgroenendaal.spotifyfacade.facades

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.AddPlaylistSongCommand
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyClientService
import nl.tijsgroenendaal.spotifyfacade.commands.AddSpotifySongCommand
import nl.tijsgroenendaal.spotifyfacade.services.SessionService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class SpotifyFacade(
    private val spotifyClientService: SpotifyClientService,
    private val userLinkFacade: UserLinkFacade,
    private val sessionService: SessionService
) {

    fun addPlaylistSong(sessionId: UUID, songId: String, command: AddSpotifySongCommand) {
        val accessToken = userLinkFacade.getAccessTokenBySessionId(sessionId)

        val playlistId = sessionService.getPlaylistIdBySessionId(sessionId)

        spotifyClientService.addSong(accessToken, playlistId, AddPlaylistSongCommand(
            listOf(songId),
            command.index
        ))
    }
}