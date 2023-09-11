package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.clients.SessionClient
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.clients.SessionSongClient
import nl.tijsgroenendaal.queuemusicfacade.commands.AcceptSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.DeleteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.EndSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.JoinSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.LeaveSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.VoteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand
import nl.tijsgroenendaal.qumu.helper.catchingFeignRequest

import java.util.UUID

import org.springframework.stereotype.Service

import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.AddSessionSongCommand as AddSessionSongClientCommand

@Service
class SessionService(
	private val sessionClient: SessionClient,
	private val sessionSongClient: SessionSongClient
) {

	fun joinSession(command: JoinSessionCommand, userId: UUID): JoinSessionCommandResponse {
		return catchingFeignRequest { sessionClient.joinSession(command.code, userId) }
	}

	fun leaveSession(command: LeaveSessionCommand, userId: UUID) {
		return catchingFeignRequest { sessionClient.leaveSession(command.sessionId, userId) }
	}

	fun endSession(command: EndSessionCommand, userId: UUID) {
		return catchingFeignRequest { sessionClient.endSession(command.sessionId, userId) }
	}

	fun createSession(command: CreateSessionCommand, userId: UUID): CreateSessionCommandResponse {
		return catchingFeignRequest { sessionClient.createSession(command, userId) }
	}

	fun createSessionSong(command: AddSessionSongCommand, userId: UUID): AddSessionSongCommandResponse {
		return catchingFeignRequest { sessionSongClient.addSong(
			command.sessionId,
			AddSessionSongClientCommand(command.trackId, command.album, command.name, command.artists),
			userId
		) }
	}

	fun vote(command: VoteSessionSongCommand, userId: UUID): VoteSessionSongCommandResponse {
		return catchingFeignRequest { sessionSongClient.voteSong(command.sessionId, command.songId, command.vote, userId) }
	}

	fun deleteSessionSong(command: DeleteSessionSongCommand, userId: UUID) {
		return catchingFeignRequest { sessionSongClient.deleteSong(command.sessionId, command.songId, userId) }
	}

	fun acceptSessionSong(command: AcceptSessionSongCommand, userId: UUID) {
		return catchingFeignRequest { sessionSongClient.acceptSong(command.sessionId, command.songId, userId) }
	}
}