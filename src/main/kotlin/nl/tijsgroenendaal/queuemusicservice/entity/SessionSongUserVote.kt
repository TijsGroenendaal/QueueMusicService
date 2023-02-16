package nl.tijsgroenendaal.queuemusicservice.entity

import nl.tijsgroenendaal.queuemusicservice.entity.enums.VoteEnum

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.util.UUID

@Entity(
    name = "queuemusic_session_uservote"
)
class SessionSongUserVote(
    @Id
    val id: UUID,
    @ManyToOne
    val userDeviceLink: UserDeviceLink,
    @ManyToOne
    val song: SessionSong,
    @Enumerated(EnumType.STRING)
    val vote: VoteEnum
)