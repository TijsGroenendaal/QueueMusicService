package nl.tijsgroenendaal.sessionservice.controllers

import nl.tijsgroenendaal.sessionservice.commands.CreateSessionCommand
import nl.tijsgroenendaal.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.sessionservice.commands.responses.CreateSessionCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.sessionservice.commands.responses.JoinSessionCommandResponse.Companion.toResponse
import nl.tijsgroenendaal.sessionservice.facades.SessionFacade

import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.UUID

@RestController
@RequestMapping("/v1/sessions")
class SessionController(
    private val sessionFacade: SessionFacade
) {

    @PostMapping
    @PreAuthorize("hasAuthority('SPOTIFY')")
    fun createSession(
        @RequestBody command: CreateSessionCommand,
        @RequestParam userId: UUID
    ): CreateSessionCommandResponse {
        return sessionFacade.createSession(command, userId).toResponse()
    }

    @PostMapping("/{code}/join")
    fun joinSession(
        @PathVariable code: String,
        @RequestParam userId: UUID
    ): JoinSessionCommandResponse {
        return sessionFacade.joinSession(code, userId).toResponse()
    }

    @DeleteMapping("/{sessionId}/leave")
    fun leaveSession(
        @PathVariable sessionId: UUID,
        @RequestParam userId: UUID
    ) {
        sessionFacade.leaveSession(sessionId, userId)
    }

    @PreAuthorize("hasAuthority('SPOTIFY')")
    @PutMapping("/{sessionId}/close")
    fun endSession(
        @PathVariable sessionId: UUID,
        @RequestParam userId: UUID
    ) {
        sessionFacade.endSession(sessionId, userId)
    }
}