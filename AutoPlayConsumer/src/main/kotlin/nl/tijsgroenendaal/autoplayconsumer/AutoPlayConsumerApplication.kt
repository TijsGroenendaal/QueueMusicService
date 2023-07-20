package nl.tijsgroenendaal.autoplayconsumer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients

@SpringBootApplication
@EnableFeignClients
class AutoPlayConsumerApplication

fun main(args: Array<String>) {
    runApplication<AutoPlayConsumerApplication>(*args)
}
