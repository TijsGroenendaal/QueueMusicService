package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration

import nl.tijsgroenendaal.queuemusicservice.facades.UserLinkFacade
import nl.tijsgroenendaal.queuemusicservice.helper.getAuthenticationContextSubject

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyApiClientConfiguration {

    @Bean
    fun feignRequestInterceptor(userLinkFacade: UserLinkFacade) = SpotifyApiClientRequestInterceptor(userLinkFacade)

}

class SpotifyApiClientRequestInterceptor(
    private val userLinkFacade: UserLinkFacade
) : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val accessToken = userLinkFacade.getAccessToken(getAuthenticationContextSubject())

        requestTemplate.header("Authorization", "Bearer $accessToken")
    }
}