package nl.tijsgroenendaal.queuemusicfacade

import nl.tijsgroenendaal.queuemusicfacade.clients.spotify_client.configuration.ClientIdConfiguration

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"])
@EnableJpaRepositories
@EnableFeignClients
@EnableConfigurationProperties(ClientIdConfiguration::class)
class QueueMusicFacade

fun main(args: Array<String>) {
    runApplication<QueueMusicFacade>(*args)
}