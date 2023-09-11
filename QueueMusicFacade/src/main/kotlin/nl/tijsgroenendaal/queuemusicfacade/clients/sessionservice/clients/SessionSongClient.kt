package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.AddSessionSongCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.AddSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.configuration.SessionServiceConfiguration
import java.util.UUID
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
	url = "\${clients.session-service}",
	name = "session-service-session-song-client",
	configuration = [SessionServiceConfiguration::class]
)
interface SessionSongClient {

	@PostMapping("/v1/sessions/{sessionId}/songs")
	fun addSong(
		@PathVariable sessionId: UUID,
		@RequestBody command: AddSessionSongCommand,
		@RequestParam userId: UUID
	): AddSessionSongCommandResponse

	@PutMapping("/v1/sessions/{sessionId}/songs/{songId}")
	fun voteSong(
		@PathVariable sessionId: UUID,
		@PathVariable songId: UUID,
		@RequestParam vote: String,
		@RequestParam userId: UUID
	): VoteSessionSongCommandResponse

	@DeleteMapping("/v1/sessions/{sessionId}/songs/{songId}")
	fun deleteSong(
		@PathVariable sessionId: UUID,
		@PathVariable songId: UUID,
		@RequestParam userId: UUID
	)

	@PutMapping("/v1/sessions/{sessionId}/songs/{songId}/accept")
	fun acceptSong(
		@PathVariable sessionId: UUID,
		@PathVariable songId: UUID,
		@RequestParam userId: UUID
	)
}