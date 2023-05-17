package nl.tijsgroenendaal.spotifyfacade

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"], exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableJpaRepositories
@EnableFeignClients
class SpotifyFacade

fun main(args: Array<String>) {
    runApplication<SpotifyFacade>(*args)
}
