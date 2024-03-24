package nl.tijsgroenendaal.sessionservice.commands.responses

import nl.tijsgroenendaal.sessionservice.entity.SessionModel

import java.time.LocalDateTime
import java.util.UUID

data class CreateSessionCommandResponse(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val duration: Long,
    val code: String,
    val manualEnded: Boolean
) {
    companion object {
        fun SessionModel.toResponse(): CreateSessionCommandResponse {
            return CreateSessionCommandResponse(
                this.id,
                this.createdAt,
                this.endAt,
                this.duration,
                this.code,
                this.manualEnded
            )
        }
    }
}
