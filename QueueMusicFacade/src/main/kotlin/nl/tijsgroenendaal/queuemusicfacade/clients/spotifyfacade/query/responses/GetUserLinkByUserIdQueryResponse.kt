package nl.tijsgroenendaal.queuemusicfacade.clients.spotifyfacade.query.responses

import java.util.UUID

data class GetUserLinkByUserIdQueryResponse(
    val id: UUID,
    val userModelId: UUID,
    val linkId: String
)
