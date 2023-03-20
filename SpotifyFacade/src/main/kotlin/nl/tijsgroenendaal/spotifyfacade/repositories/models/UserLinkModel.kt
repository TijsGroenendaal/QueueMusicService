package nl.tijsgroenendaal.spotifyfacade.repositories.models

import java.time.LocalDateTime
import java.util.UUID

data class UserLinkModel(
    val id: UUID,
    val linkAccessToken: String,
    val linkExpireTime: LocalDateTime,
    val linkId: String,
    val linkRefreshToken: String
)
