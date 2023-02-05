package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyTokenClientConfiguration {

    @Bean
    fun feignRequestInterceptor(clientIdConfiguration: ClientIdConfiguration) = SpotifyTokenClientRequestInterceptor(clientIdConfiguration)

}

class SpotifyTokenClientRequestInterceptor(
    private val clientIdConfiguration: ClientIdConfiguration
): RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        requestTemplate.header("Authorization", clientIdConfiguration.getBasicAuth())
    }

}