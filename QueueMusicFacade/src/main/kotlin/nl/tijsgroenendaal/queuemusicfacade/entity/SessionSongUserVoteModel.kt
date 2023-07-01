package nl.tijsgroenendaal.queuemusicfacade.entity

import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponseSong
import nl.tijsgroenendaal.queuemusicfacade.commands.responses.VoteSessionSongCommandResponseUser
import nl.tijsgroenendaal.queuemusicfacade.entity.enums.VoteEnum

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.util.UUID

@Entity(
    name = "queuemusic_session_uservote"
)
class SessionSongUserVoteModel(
    @Id
    val id: UUID,
    @ManyToOne
    val user: UserModel,
    @ManyToOne
    val song: SessionSongModel,
    @Enumerated(EnumType.STRING)
    var vote: VoteEnum
) {
    companion object {
        fun new(song: SessionSongModel, user: UserModel, vote: VoteEnum): SessionSongUserVoteModel {
            return SessionSongUserVoteModel(
                UUID.randomUUID(),
                user,
                song,
                vote
            )
        }

        fun SessionSongUserVoteModel.toResponse(): VoteSessionSongCommandResponse {
            return VoteSessionSongCommandResponse(
                this.id,
                VoteSessionSongCommandResponseSong(
                    this.song.id,
                    this.song.trackId,
                    this.song.title,
                    this.song.album,
                    this.song.authors,
                    this.song.createdAt
                ),
                VoteSessionSongCommandResponseUser(
                    this.user.id
                ),
                this.vote
            )
        }

    }
}