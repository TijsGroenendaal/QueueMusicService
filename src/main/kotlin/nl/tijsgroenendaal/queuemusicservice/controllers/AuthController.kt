package nl.tijsgroenendaal.queuemusicservice.controllers

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/auth")
class AuthController(

) {

    @PostMapping()
    fun login(
        @RequestParam("code") code: String
    ) {

    }

    @PostMapping("/refresh")
    fun refresh(
        @RequestParam("token") refreshToken: String
    ) {

    }

    @PostMapping("/logout")
    fun logout() {

    }

}