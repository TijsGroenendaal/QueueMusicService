package nl.tijsgroenendaal.spotifyfacade

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@EnableConfigurationProperties
@SpringBootApplication
class SpotifyFacadeApplication

fun main(args: Array<String>) {
    runApplication<SpotifyFacadeApplication>(*args)
}
