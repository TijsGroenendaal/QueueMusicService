package nl.tijsgroenendaal.idpservice.controllers

import nl.tijsgroenendaal.idpservice.facades.UserFacade
import nl.tijsgroenendaal.idpservice.query.responses.SpotifyMeResponse
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user")
class UserController(
	private val userFacade: UserFacade
) {

	@GetMapping("/me")
	@PreAuthorize("hasAuthority('SPOTIFY')")
	fun me(): SpotifyMeResponse {
		return userFacade.me()
	}

}