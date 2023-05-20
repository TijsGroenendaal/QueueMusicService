package nl.tijsgroenendaal.queuemusicfacade

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"])
@EnableJpaRepositories
@EnableFeignClients
class QueueMusicFacade

fun main(args: Array<String>) {
    runApplication<QueueMusicFacade>(*args)
}