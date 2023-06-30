package nl.tijsgroenendaal.queuemusicfacade.entity

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
    val vote: VoteEnum
)