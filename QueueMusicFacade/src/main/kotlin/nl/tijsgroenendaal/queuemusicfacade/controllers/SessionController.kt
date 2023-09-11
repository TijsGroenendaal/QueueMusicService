package nl.tijsgroenendaal.queuemusicfacade.controllers

import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.facades.SessionFacade

import java.util.UUID

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import nl.tijsgroenendaal.queuemusicfacade.commands.EndSessionCommand as EndSessionFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand as CreateSessionFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.LeaveSessionCommand as LeaveSessionFacadeCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.JoinSessionCommand as JoinSessionFacadeCommand

@RestController
@RequestMapping("/v1/sessions")
class SessionController(
    private val sessionFacade: SessionFacade
) {

    @PostMapping()
    @PreAuthorize("hasAuthority('SPOTIFY')")
    fun createSession(
        @RequestBody command: CreateSessionFacadeCommand
    ): CreateSessionCommandResponse {
        return sessionFacade.createSession(command)
    }

    @PostMapping("/{code}/join")
    fun joinSession(
        @PathVariable code: String
    ): JoinSessionCommandResponse {
        return sessionFacade.joinSession(JoinSessionFacadeCommand(
            code
        ))
    }

    @DeleteMapping("/{sessionId}/leave")
    fun leaveSession(
        @PathVariable sessionId: UUID
    ) {
        sessionFacade.leaveSession(LeaveSessionFacadeCommand(
            sessionId
        ))
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{sessionId}/close")
    fun endSession(
        @PathVariable sessionId: UUID
    ) {
        sessionFacade.endSession(EndSessionFacadeCommand(
            sessionId
        ))
    }

}