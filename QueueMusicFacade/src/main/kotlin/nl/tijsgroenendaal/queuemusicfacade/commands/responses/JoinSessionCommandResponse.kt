package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel

import java.time.LocalDateTime
import java.util.UUID

data class JoinSessionCommandResponse(
    val id: UUID,
    val device: JoinSessionCommandResponseDevice,
    val session: JoinSessionCommandResponseSession
) {
    companion object {
        fun SessionUserModel.toResponse(): JoinSessionCommandResponse {
            return JoinSessionCommandResponse(
                this.id,
                JoinSessionCommandResponseDevice(
                    this.deviceLink.id,
                    this.deviceLink.deviceId
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

data class JoinSessionCommandResponseDevice(
    val id: UUID,
    val deviceId: String
)