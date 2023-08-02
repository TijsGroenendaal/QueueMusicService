package nl.tijsgroenendaal.idpservice.controllers

import nl.tijsgroenendaal.idpservice.commands.GenerateJwtCommand
import nl.tijsgroenendaal.idpservice.facades.JwtFacade
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1")
class JwtController(
    private val jwtFacade: JwtFacade
) {

    @PostMapping("/secure/client-jwt")
    fun generateJwtForClient(@RequestBody command: GenerateJwtCommand) = jwtFacade.generateJwtForClient(command)

}