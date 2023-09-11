package nl.tijsgroenendaal.queuemusicfacade.facades

import nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.services.SpotifyService
import nl.tijsgroenendaal.queuemusicfacade.commands.CreateSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.EndSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.JoinSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.LeaveSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.services.SessionService
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContextSubject

import java.util.Random

import org.springframework.stereotype.Service

import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand as CreateSessionServiceCommand
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommandAutoplay as CreateSessionServiceCommandAutoplay

private val ALLOWED_CODE_CHARS = ('A'..'Z') + ('a'..'z').toList()
private const val SESSION_CODE_LENGTH = 8

@Service
class SessionFacade(
    private val spotifyService: SpotifyService,
    private val sessionService: SessionService
) {

    fun createSession(command: CreateSessionCommand): CreateSessionCommandResponse {
        val userId = getAuthenticationContextSubject()
        val sessionCode = generateSessionCode()

        val playlist = if (command.autoplay != null) {
            spotifyService.createPlaylist(sessionCode, userId)
        } else null

        try {
            return sessionService.createSession(CreateSessionServiceCommand(
                command.duration,
                command.maxUsers,
                playlist?.let { CreateSessionServiceCommandAutoplay(
                    playlist.id,
                    command.autoplay!!.acceptance
                )}
            ), userId)
        } catch (e: Exception) {
            TODO("If creation fails the spotifyplaylist should be deleted....")
        }
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

    private fun generateSessionCode(): String {
        val random = Random()
        val allowedCharSize = ALLOWED_CODE_CHARS.size - 1
        return String((0..SESSION_CODE_LENGTH).map { ALLOWED_CODE_CHARS[random.nextInt(allowedCharSize)] }.toCharArray())
    }
}