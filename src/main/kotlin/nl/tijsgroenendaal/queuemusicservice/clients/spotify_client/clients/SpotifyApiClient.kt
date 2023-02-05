package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    url = "\${clients.spotify-api}",
    name = "spotify-api-client",
    configuration = []
)
interface SpotifyApiClient {



}