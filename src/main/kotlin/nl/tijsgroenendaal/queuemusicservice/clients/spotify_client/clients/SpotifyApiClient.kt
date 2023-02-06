package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyApiClientConfiguration

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    url = "\${clients.spotify-api}",
    name = "spotify-api-client",
    configuration = [SpotifyApiClientConfiguration::class]
)
interface SpotifyApiClient {



}