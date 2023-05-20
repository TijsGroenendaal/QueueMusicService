package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.CreatePlaylistCommand
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration.SpotifyApiClientConfiguration
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    name = "spotify-api-client",
    url = "\${clients.spotify-api}",
    configuration = [SpotifyApiClientConfiguration::class]
)
interface SpotifyApiClient {

    @GetMapping("/v1/me")
    fun getMe(): GetMeQueryResponse

    @PostMapping("/v1/users/{userId}/playlists")
    fun createPlaylists(@PathVariable userId: String, @RequestBody command: CreatePlaylistCommand): CreatePlaylistCommandResponse

}