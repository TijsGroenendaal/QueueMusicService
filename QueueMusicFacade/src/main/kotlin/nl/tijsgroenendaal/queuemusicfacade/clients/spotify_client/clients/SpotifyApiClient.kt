package nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.commands.CreatePlaylistCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.commands.responses.CreatePlaylistCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.configuration.SpotifyApiClientConfiguration
import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.query.responses.users.GetMeQueryResponse

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${clients.spotify-api}",
    name = "spotify-api-client",
    configuration = [SpotifyApiClientConfiguration::class]
)
interface SpotifyApiClient {

    @GetMapping("/v1/me")
    fun getMe(): GetMeQueryResponse
    @GetMapping("/v1/me/playlists")
    fun getMyPlaylists(): Any
    @PostMapping("/v1/users/{userId}/playlists")
    fun createPlaylists(@PathVariable userId: String, @RequestBody command: CreatePlaylistCommand): CreatePlaylistCommandResponse

}