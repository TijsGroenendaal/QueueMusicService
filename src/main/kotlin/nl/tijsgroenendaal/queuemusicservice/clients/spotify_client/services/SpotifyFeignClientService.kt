package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.services

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients.SpotifyApiClient

import org.springframework.stereotype.Service

@Service
class SpotifyFeignClientService(
    private val spotifyApiClient: SpotifyApiClient
) {
}