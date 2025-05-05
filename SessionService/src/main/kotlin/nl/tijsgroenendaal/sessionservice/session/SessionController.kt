package nl.tijsgroenendaal.sessionservice.session

import nl.tijsgroenendaal.sessionservice.requests.responses.CreateSessionResponse
import nl.tijsgroenendaal.sessionservice.requests.responses.GetSessionResponse
import nl.tijsgroenendaal.sessionservice.requests.responses.JoinSessionResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
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
        @RequestBody command: CreateSessionControllerRequest,
        @RequestParam userId: UUID
    ): CreateSessionResponse {
        val result = sessionFacade.createSession(command, userId)

        return CreateSessionResponse(result)
    }

    @PostMapping("/{code}/join")
    fun joinSession(
        @PathVariable code: String,
        @RequestParam userId: UUID
    ): JoinSessionResponse {
        val result = sessionFacade.joinSession(code, userId)
        return JoinSessionResponse(result)
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

    @GetMapping("/current")
    fun current(
        @RequestParam userId: UUID
    ): GetSessionResponse {
        val session = sessionFacade.current(userId)
        return GetSessionResponse(session)
    }

    @GetMapping("/{sessionId}")
    fun getSession(
        @PathVariable sessionId: UUID,
        @RequestParam userId: UUID
    ): GetSessionResponse {
        val session = sessionFacade.getSession(sessionId, userId)
        return GetSessionResponse(session)
    }
}