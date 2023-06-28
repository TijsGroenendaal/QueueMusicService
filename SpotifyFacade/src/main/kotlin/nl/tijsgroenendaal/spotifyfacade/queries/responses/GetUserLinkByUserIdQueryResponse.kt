package nl.tijsgroenendaal.spotifyfacade.queries.responses

import java.util.UUID

data class GetUserLinkByUserIdQueryResponse(
    val id: UUID,
    val userModelId: UUID,
    val linkId: String
)
