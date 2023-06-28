package nl.tijsgroenendaal.spotifyfacade.controllers

import nl.tijsgroenendaal.spotifyfacade.facades.UserLinkFacade
import nl.tijsgroenendaal.spotifyfacade.queries.responses.GetUserLinkByUserIdQueryResponse

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

import java.util.UUID

@RestController
@RequestMapping("/v1/user-link")
class UserLinkController(
    private val userLinkFacade: UserLinkFacade
) {

    @GetMapping("/user/{userId}")
    fun getByUserId(@PathVariable userId: UUID): GetUserLinkByUserIdQueryResponse {
        return userLinkFacade.getByUserId(userId)
    }

}