package nl.tijsgroenendaal.queuemusicservice.controllers

import nl.tijsgroenendaal.queuemusicservice.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicservice.facades.SessionFacade

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/sessions")
class SessionController(
    private val sessionFacade: SessionFacade
) {

    @PostMapping()
    @PreAuthorize("hasAuthority('SPOTIFY')")
    fun createSession(
        @RequestBody command: CreateSessionCommand
    ) {
        sessionFacade.createSession(command)
    }

}