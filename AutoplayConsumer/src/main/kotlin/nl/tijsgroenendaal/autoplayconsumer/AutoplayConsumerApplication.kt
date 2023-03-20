package nl.tijsgroenendaal.autoplayconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AutoplayConsumerApplication

fun main(args: Array<String>) {
    runApplication<AutoplayConsumerApplication>(*args)
}
