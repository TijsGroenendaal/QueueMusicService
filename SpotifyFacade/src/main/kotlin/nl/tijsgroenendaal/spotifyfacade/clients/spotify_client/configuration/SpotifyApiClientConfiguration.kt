package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration

import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject
import nl.tijsgroenendaal.spotifyfacade.facades.UserLinkFacade

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyApiClientConfiguration {

    @Bean
    fun feignRequestInterceptor(userLinkFacade: UserLinkFacade) = SpotifyApiClientRequestInterceptor(userLinkFacade)

}

class SpotifyApiClientRequestInterceptor(
    private val userLinkFacade: UserLinkFacade
): RequestInterceptor {

    override fun apply(template: RequestTemplate) {
        val accessToken = userLinkFacade.getAccessToken(getAuthenticationContextSubject())
        template.header("Authorization", "Bearer $accessToken")
    }
}