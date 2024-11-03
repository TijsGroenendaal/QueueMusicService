package nl.tijsgroenendaal.sessionservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(
    scanBasePackages = ["nl.tijsgroenendaal"],
    exclude = [UserDetailsServiceAutoConfiguration::class]
)
class SessionServiceApplication

fun main(args: Array<String>) {
    runApplication<SessionServiceApplication>(*args)
}
