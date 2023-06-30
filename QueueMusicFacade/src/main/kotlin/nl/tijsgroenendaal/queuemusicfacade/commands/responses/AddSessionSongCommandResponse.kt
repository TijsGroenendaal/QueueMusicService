package nl.tijsgroenendaal.queuemusicfacade.commands.responses

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel

import java.time.LocalDateTime
import java.util.UUID

data class AddSessionSongCommandResponse(
    val id: UUID,
    val user: AddSessionSongCommandResponseUser,
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
                AddSessionSongCommandResponseUser(
                    this.user.id,
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

data class AddSessionSongCommandResponseUser(
    val id: UUID,
)

data class AddSessionSongCommandResponseSession(
    val id: UUID,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String
)
