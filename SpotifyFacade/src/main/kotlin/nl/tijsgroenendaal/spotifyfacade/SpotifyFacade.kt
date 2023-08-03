package nl.tijsgroenendaal.spotifyfacade

import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.configuration.ClientIdConfiguration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"], exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableJpaRepositories
@EnableFeignClients
@EnableConfigurationProperties(ClientIdConfiguration::class)
@EnableMethodSecurity
class SpotifyFacade

fun main(args: Array<String>) {
    runApplication<SpotifyFacade>(*args)
}
