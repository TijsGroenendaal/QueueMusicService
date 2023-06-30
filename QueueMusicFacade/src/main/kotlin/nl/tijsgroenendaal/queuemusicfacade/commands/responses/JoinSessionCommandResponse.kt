package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel

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
                    this.user.id,
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