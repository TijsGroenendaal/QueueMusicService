package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.configuration

import nl.tijsgroenendaal.queuemusicfacade.services.IdpService

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean

class SessionServiceConfiguration {

    @Bean
    fun feignRequestInterceptor(
			idpService: IdpService,
			@Value("\${clients.idp.session-service.id}")
        clientId: String,
			@Value("\${clients.idp.session-service.secret}")
        clientSecret: String
    ) = SessionServiceRequestInterceptor(idpService, clientId, clientSecret)
}

class SessionServiceRequestInterceptor(
		private val idpService: IdpService,
		private val clientId: String,
		private val clientSecret: String
) : RequestInterceptor {

    override fun apply(request: RequestTemplate) {
        val jwt = idpService.generateClientJwt(clientId, clientSecret).token
        request.header("Authorization", "Bearer $jwt")
    }
}