package nl.tijsgroenendaal.autoplayconsumer.clients.spotifyfacade.configuration

import nl.tijsgroenendaal.autoplayconsumer.services.IdpService

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class SpotifyFacadeConfiguration {

    @Bean
    fun feignRequestInterceptor(idpService: IdpService) = SpotifyFacadeRequestInterceptor(idpService)
}

class SpotifyFacadeRequestInterceptor(
		private val idpService: IdpService,
) : RequestInterceptor {

    override fun apply(request: RequestTemplate) {
        val jwt = idpService.generateClientJwt().token
        request.header("Authorization", "Bearer $jwt")
    }
}