package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.CreateSessionCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.JoinSessionCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.queuemusicfacade.facades.SessionFacade

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
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
    ): CreateSessionCommandResponse {
        return sessionFacade.createSession(command).toResponse()
    }

    @PostMapping("/{code}/join")
    fun joinSession(
        @PathVariable code: String
    ): JoinSessionCommandResponse {
        return sessionFacade.joinSession(code).toResponse()
    }

    @DeleteMapping("/{code}/leave")
    fun leaveSession(
        @PathVariable code: String
    ) {
        sessionFacade.leaveSession(code)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{code}/close")
    fun endSession(
        @PathVariable code: String
    ) {
        sessionFacade.endSession(code)
    }

}