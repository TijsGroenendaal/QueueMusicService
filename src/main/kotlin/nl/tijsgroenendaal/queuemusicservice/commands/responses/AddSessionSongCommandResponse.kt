package nl.tijsgroenendaal.queuemusicservice.commands.responses

import nl.tijsgroenendaal.queuemusicservice.entity.SessionSongModel

import java.time.LocalDateTime
import java.util.UUID

data class AddSessionSongCommandResponse(
    val id: UUID,
    val device: AddSessionSongCommandResponseDevice,
    val session: AddSessionSongCommandResponseSession,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val votes: Int,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun SessionSongModel.toResponse(): AddSessionSongCommandResponse {
            return AddSessionSongCommandResponse(
                this.id,
                AddSessionSongCommandResponseDevice(
                    this.deviceLink.id,
                    this.deviceLink.deviceId
                ),
                AddSessionSongCommandResponseSession(
                    this.session.id,
                    this.session.createdAt,
                    this.session.endAt,
                    this.session.code
                ),
                this.trackId,
                this.title,
                this.album,
                this.authors,
                this.votes,
                this.createdAt
            )
        }
    }
}

data class AddSessionSongCommandResponseDevice(
    val id: UUID,
    val deviceId: String
)

data class AddSessionSongCommandResponseSession(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String
)
