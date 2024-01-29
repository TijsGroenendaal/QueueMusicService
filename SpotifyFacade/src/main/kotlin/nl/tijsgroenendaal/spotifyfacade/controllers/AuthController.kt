package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.facades.AuthFacade

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.UUID

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authFacade: AuthFacade
) {

    @PostMapping("/login")
    fun login(@RequestParam(name = "code") code: String, @RequestParam(name = "redirect_uri") redirectUri: String): UUID {
        return authFacade.login(code, redirectUri).userModelId
    }

    @PostMapping("/logout")
    fun logout() {
        authFacade.logout()
    }

}