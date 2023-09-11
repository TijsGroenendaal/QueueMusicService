package nl.tijsgroenendaal.sessionservice.commands.responses

import nl.tijsgroenendaal.sessionservice.entity.SessionUserModel

import java.time.LocalDateTime
import java.util.UUID

data class JoinSessionCommandResponse(
        val user: JoinSessionCommandResponseUser,
        val session: JoinSessionCommandResponseSession
) {
    companion object {
        fun SessionUserModel.toResponse(): JoinSessionCommandResponse {
            return JoinSessionCommandResponse(
                JoinSessionCommandResponseUser(
                    this.user,
                ),
                JoinSessionCommandResponseSession(
                    this.session.id,
                    this.session.createdAt,
                    this.session.endAt,
                    this.session.code
                )
            )
        }
    }
}

data class JoinSessionCommandResponseSession(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String
)

data class JoinSessionCommandResponseUser(
    val id: UUID,
)