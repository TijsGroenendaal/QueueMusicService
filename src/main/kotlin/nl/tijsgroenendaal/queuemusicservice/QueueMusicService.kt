package nl.tijsgroenendaal.queuemusicservice

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.ClientIdConfiguration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication
@EnableJpaRepositories
@EnableFeignClients
@EnableConfigurationProperties(ClientIdConfiguration::class)
class QueueMusicService

fun main(args: Array<String>) {
    runApplication<QueueMusicService>(*args)
}