package nl.tijsgroenendaal.idpservice

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class IdpServiceApplication

fun main(args: Array<String>) {
    runApplication<IdpServiceApplication>(*args)
}
