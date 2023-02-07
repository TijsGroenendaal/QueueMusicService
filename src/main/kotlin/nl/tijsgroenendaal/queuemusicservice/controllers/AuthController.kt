package nl.tijsgroenendaal.queuemusicservice.controllers

import nl.tijsgroenendaal.queuemusicservice.facades.AuthFacade
import nl.tijsgroenendaal.queuemusicservice.query.responses.LoginQueryResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(
    private val authFacade: AuthFacade
) {

    @PostMapping("/login")
    fun login(
        @RequestParam("code") code: String
    ): LoginQueryResponse {
        return authFacade.loginLinkUser(code)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestParam("token") refreshToken: String
    ): LoginQueryResponse {
        return authFacade.refresh(refreshToken)
    }

    @PostMapping("/logout")
    fun logout() {

    }

}