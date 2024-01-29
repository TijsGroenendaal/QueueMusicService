package nl.tijsgroenendaal.idpservice.controllers

import nl.tijsgroenendaal.idpservice.facades.AuthFacade
import nl.tijsgroenendaal.idpservice.query.responses.LoginQueryResponse

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
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
        @RequestParam("code") code: String,
        @RequestParam("redirect_uri") redirectUri: String
    ): LoginQueryResponse {
        return authFacade.loginLinkUser(code, redirectUri)
    }

    @PostMapping("/login/anonymous")
    fun loginAnonymous(
        @RequestParam("deviceId") deviceId: String
    ): LoginQueryResponse {
       return authFacade.loginAnonymous(deviceId)
    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestHeader("Authorization") refreshToken: String
    ): LoginQueryResponse {
        return authFacade.refresh(refreshToken)
    }

    @PostMapping("/logout")
    fun logout() {
        authFacade.logout()
    }
}