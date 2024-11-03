package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import java.time.Instant
import java.util.UUID

data class GetCurrentSessionResponse(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String
) {

    constructor(sessionModel: SessionModel) : this(
        sessionModel.id,
        sessionModel.createdAt,
        sessionModel.endAt,
        sessionModel.code
    )
}
