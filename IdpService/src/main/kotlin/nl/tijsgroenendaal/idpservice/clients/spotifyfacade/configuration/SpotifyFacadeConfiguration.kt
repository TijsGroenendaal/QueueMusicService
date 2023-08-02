package nl.tijsgroenendaal.idpservice.clients.spotifyfacade.configuration

import nl.tijsgroenendaal.idpservice.commands.GenerateClientTokenCommand
import nl.tijsgroenendaal.idpservice.facades.JwtFacade

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpotifyFacadeConfiguration(
    @Value("\${clients.idp.spotify-facade.id}")
    private val spotifyFacadeId: String,
    @Value("\${clients.idp.spotify-facade.secret}")
    private val spotifyFacadeSecret: String
) {

    @Bean
    fun feignRequestInterceptor(jwtFacade: JwtFacade) = SpotifyFacadeRequestInterceptor(jwtFacade, spotifyFacadeSecret, spotifyFacadeId)
}

class SpotifyFacadeRequestInterceptor(
    private val jwtFacade: JwtFacade,
    private val spotifyFacadeSecret: String,
    private val spotifyFacadeId: String
) : RequestInterceptor {

    override fun apply(request: RequestTemplate) {
        val command = GenerateClientTokenCommand(
            spotifyFacadeId,
            spotifyFacadeSecret
        )

        request.header("Authorization", jwtFacade.generateJwtForClient(command).token)
    }
}