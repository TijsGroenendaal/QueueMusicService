package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserModel
import java.time.Instant
import java.util.UUID

data class JoinSessionResponse(
    val user: JoinSessionResponseUser,
    val session: JoinSessionResponseSession
) {
    constructor(sessionUserModel: SessionUserModel) : this(
        JoinSessionResponseUser(sessionUserModel.user),
        JoinSessionResponseSession(sessionUserModel.session)
    )
}

data class JoinSessionResponseSession(
    val id: UUID,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String
) {
    constructor(session: SessionModel) : this(
        session.id,
        session.createdAt,
        session.endAt,
        session.code
    )
}

data class JoinSessionResponseUser(
    val id: UUID,
)