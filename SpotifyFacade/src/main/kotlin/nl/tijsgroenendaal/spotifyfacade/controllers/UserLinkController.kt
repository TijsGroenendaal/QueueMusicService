package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.services.UserLinkService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/user-link")
class UserLinkController(
    private val userLinkService: UserLinkService
) {

    @PostMapping("/logout")
    fun logout() {
        userLinkService.logout()
    }
}