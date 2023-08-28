package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.commands.AcceptSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.DeleteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.EndSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.JoinSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.LeaveSessionCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.VoteSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponse

import java.util.UUID

import org.springframework.stereotype.Service

@Service
class SessionService {
	fun createNew(command: JoinSessionCommand, authenticationContextSubject: UUID): JoinSessionCommandResponse {
		TODO("Not yet implemented")
	}

	fun leaveSession(command: LeaveSessionCommand, authenticationContextSubject: UUID) {
		TODO("Not yet implemented")
	}

	fun endSession(command: EndSessionCommand, authenticationContextSubject: UUID) {
		TODO("Not yet implemented")
	}

	fun createSession(createSessionCommand: Any): CreateSessionCommandResponse {
		TODO("Not yet implemented")
	}

	fun createSessionSong(command: AddSessionSongCommand, userId: UUID): AddSessionSongCommandResponse {
		TODO("Not yet implemented")
	}

	fun vote(command: VoteSessionSongCommand, authenticationContextSubject: UUID): VoteSessionSongCommandResponse {
		TODO("Not yet implemented")
	}

	fun deleteSessionSong(command: DeleteSessionSongCommand, authenticationContextSubject: UUID) {
		TODO("Not yet implemented")
	}

	fun acceptSessionSong(command: AcceptSessionSongCommand, authenticationContextSubject: UUID) {
		TODO("Not yet implemented")
	}


}