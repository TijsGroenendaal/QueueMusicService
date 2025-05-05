package nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.CreateSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.commands.responses.JoinSessionCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.configuration.SessionServiceConfiguration
import nl.tijsgroenendaal.queuemusicfacade.clients.sessionservice.query.responses.GetSessionResponse
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand

import java.util.UUID

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
	url = "\${clients.session-service}",
	name = "session-service-session-client",
	configuration = [SessionServiceConfiguration::class]
)
interface SessionClient {

	@PostMapping("/v1/sessions")
	fun createSession(@RequestBody command: CreateSessionCommand, @RequestParam("userId") userId: UUID): CreateSessionCommandResponse

	@PostMapping("/v1/sessions/{code}/join")
	fun joinSession(@PathVariable code: String, @RequestParam userId: UUID):  JoinSessionCommandResponse

	@DeleteMapping("/v1/sessions/{sessionId}/leave")
	fun leaveSession(@PathVariable sessionId: UUID, @RequestParam userId: UUID)

	@PutMapping("/v1/sessions/{sessionId}/close")
	fun endSession(@PathVariable sessionId: UUID, @RequestParam userId: UUID)

	@GetMapping("/v1/sessions/current")
	fun getCurrent(@RequestParam userId: UUID): GetSessionResponse

	@GetMapping("/v1/sessions/{sessionId}")
	fun getSession(@PathVariable sessionId: UUID, @RequestParam userId: UUID): GetSessionResponse
}