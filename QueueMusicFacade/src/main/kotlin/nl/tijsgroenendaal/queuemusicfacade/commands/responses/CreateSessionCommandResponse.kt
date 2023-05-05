package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import nl.tijsgroenendaal.queuemusicfacade.entity.QueueMusicSessionModel

import java.time.LocalDateTime
import java.util.UUID

data class CreateSessionCommandResponse(
    val id: UUID,
    val playlistId: String,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val duration: Long,
    val code: String,
    val manualEnded: Boolean
) {
    companion object {
        fun QueueMusicSessionModel.toResponse(): CreateSessionCommandResponse {
            return CreateSessionCommandResponse(
                this.id,
                this.playListId,
                this.createdAt,
                this.endAt,
                this.duration,
                this.code,
                this.manualEnded
            )
        }
    }
}
