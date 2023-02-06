package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration

import feign.Logger
import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyTokenClientConfiguration {

    @Bean
    fun feignRequestInterceptor(clientIdConfiguration: ClientIdConfiguration) = SpotifyTokenClientRequestInterceptor(clientIdConfiguration)

}

class FeignConfig {
    @Bean
    fun feignLoggerLevel(): Logger.Level {
        return Logger.Level.FULL
    }
}

class SpotifyTokenClientRequestInterceptor(
    private val clientIdConfiguration: ClientIdConfiguration
): RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val basicAuth = clientIdConfiguration.getBasicAuth()

        requestTemplate.header("Authorization", "Basic $basicAuth")
        requestTemplate.header("Content-Type","application/x-www-form-urlencoded")
    }

}