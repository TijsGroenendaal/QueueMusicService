package nl.tijsgroenendaal.sessionservice.songvote.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import nl.tijsgroenendaal.sessionservice.song.jpa.SongModel
import java.util.UUID

@Entity(
    name = "queuemusic_session_uservote"
)
class SongVoteModel(
    @Id
    val id: UUID,
    @Column(name = "user_id")
    val user: UUID,
    @ManyToOne
    val song: SongModel,
    @Enumerated(EnumType.STRING)
    var vote: VoteEnum
) {
    companion object {
        fun new(song: SongModel, userId: UUID, vote: VoteEnum): SongVoteModel {
            return SongVoteModel(
                UUID.randomUUID(),
                userId,
                song,
                vote
            )
        }

    }
}