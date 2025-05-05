package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.EndSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.JoinSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.LeaveSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.query.responses.GetSessionResponse
import nl.tijsgroenendaal.queuemusicfacade.services.SessionService
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import org.springframework.stereotype.Service
import java.util.UUID

import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand as CreateSessionServiceCommand
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommandAutoplay as CreateSessionServiceCommandAutoplay

@Service
class SessionFacade(
		private val sessionService: SessionService
) {

    fun createSession(command: CreateSessionCommand): CreateSessionCommandResponse {
        val userId = getAuthenticationContextSubject()

        return sessionService.createSession(CreateSessionServiceCommand(
            command.duration,
            command.maxUsers,
            command.autoplay?.let { CreateSessionServiceCommandAutoplay(it.acceptance) },
        ), userId)
    }

    fun joinSession(command: JoinSessionCommand): JoinSessionCommandResponse {
        return sessionService.joinSession(command, getAuthenticationContextSubject())
    }

    fun leaveSession(command: LeaveSessionCommand) {
        sessionService.leaveSession(command, getAuthenticationContextSubject())
    }

    fun endSession(command: EndSessionCommand) {
        return sessionService.endSession(command, getAuthenticationContextSubject())
    }

    fun getCurrent(): GetSessionResponse {
        return sessionService.getCurrent(getAuthenticationContextSubject())
    }

    fun getSession(sessionId: UUID): GetSessionResponse {
        return sessionService.getSession(sessionId, getAuthenticationContextSubject())
    }
}