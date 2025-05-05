package nl.tijsgroenendaal.sessionservice.requests.responses

import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserModel
import java.time.Instant
import java.util.UUID

data class GetSessionResponse(
    val id: UUID,
    val host: UUID,
    val duration: Long,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String,
    val autoplayAcceptance: Int?,
    val maxUsers: Int,
    val manualEnded: Boolean,
    val sessionUsers: List<GetSessionResponseUser>
) {
    constructor(session: SessionModel) : this(
        session.id,
        session.host,
        session.duration,
        session.createdAt,
        session.endAt,
        session.code,
        session.autoplayAcceptance,
        session.maxUsers,
        session.manualEnded,
        session.sessionUsers.map { GetSessionResponseUser(it) }
    )
}

data class GetSessionResponseUser(
    val user: UUID,
) {
    constructor(user: SessionUserModel) : this(user.user)
}
