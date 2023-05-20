package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services.SpotifyTokenClientService

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyOpenClientConfiguration {

    @Bean
    fun feignRequestInterceptor(spotifyTokenClientService: SpotifyTokenClientService) = SpotifyOpenClientRequestInterceptor(spotifyTokenClientService)

}

class SpotifyOpenClientRequestInterceptor(
    private val spotifyTokenClientService: SpotifyTokenClientService
): RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val accessToken = spotifyTokenClientService.getCredentialsAccessToken().accessToken

        template.header("Authorization", "Bearer $accessToken")
    }
}