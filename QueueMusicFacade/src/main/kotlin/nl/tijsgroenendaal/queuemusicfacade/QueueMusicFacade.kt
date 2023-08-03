package nl.tijsgroenendaal.queuemusicfacade

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"], exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableJpaRepositories
@EnableFeignClients
@EnableMethodSecurity
class QueueMusicFacade

fun main(args: Array<String>) {
    runApplication<QueueMusicFacade>(*args)
}