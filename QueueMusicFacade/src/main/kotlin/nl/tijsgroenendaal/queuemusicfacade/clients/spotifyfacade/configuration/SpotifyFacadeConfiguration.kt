package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.configuration

import nl.tijsgroenendaal.queuemusicfacade.services.IdpService

import feign.RequestInterceptor
import feign.RequestTemplate
import org.springframework.beans.factory.annotation.Value

import org.springframework.context.annotation.Bean

class SpotifyFacadeConfiguration {

    @Bean
    fun feignRequestInterceptor(
			idpService: IdpService,
			@Value("\${clients.idp.spotify-facade.id}")
            clientId: String,
			@Value("\${clients.idp.spotify-facade.secret}")
            clientSecret: String
    ) = SpotifyFacadeRequestInterceptor(idpService, clientId, clientSecret)
}

class SpotifyFacadeRequestInterceptor(
		private val idpService: IdpService,
		private val clientId: String,
		private val clientSecret: String
) : RequestInterceptor {

    override fun apply(request: RequestTemplate) {
        val jwt = idpService.generateClientJwt(clientId, clientSecret).token
        request.header("Authorization", "Bearer $jwt")
    }
}