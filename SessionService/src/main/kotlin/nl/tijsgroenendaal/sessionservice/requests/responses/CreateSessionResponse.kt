package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import java.time.Instant

import java.util.UUID

data class CreateSessionResponse(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val duration: Long,
    val code: String,
    val manualEnded: Boolean
) {
    constructor(sessionModel: SessionModel) : this(
        sessionModel.id,
        sessionModel.createdAt,
        sessionModel.endAt,
        sessionModel.duration,
        sessionModel.code,
        sessionModel.manualEnded
    )
}
