package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.AddPlaylistSongCommand
import org.springframework.stereotype.Service

@Service
class SpotifyClientService(
    private val spotifyClient: SpotifyClient
) {

    fun addSong(accessToken: String, playlistId: String, addPlaylistSongCommand: AddPlaylistSongCommand) {
        spotifyClient.addPlaylistSong(accessToken, playlistId, addPlaylistSongCommand)
    }

}