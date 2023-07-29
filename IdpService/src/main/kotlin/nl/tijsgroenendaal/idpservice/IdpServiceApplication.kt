package nl.tijsgroenendaal.idpservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@SpringBootApplication(scanBasePackages = ["nl.tijsgroenendaal"], exclude = [UserDetailsServiceAutoConfiguration::class])
@EnableJpaRepositories
@EnableFeignClients
class IdpServiceApplication

fun main(args: Array<String>) {
    runApplication<IdpServiceApplication>(*args)
}
