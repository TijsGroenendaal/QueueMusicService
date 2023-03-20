package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.AddPlaylistSongCommand

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader

@FeignClient(
    name = "spotify-client",
    url = "\${clients.spotify-api}"
)
interface SpotifyClient {
    @PostMapping("/playlists/{playlistId}/tracks")
    fun addPlaylistSong(@RequestHeader(value = "Authorization", required = true) accessToken: String, @PathVariable playlistId: String, @RequestBody addPlaylistSongCommand: AddPlaylistSongCommand)

}