package nl.tijsgroenendaal.queuemusicservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.util.UUID

@Entity(
    name = "queuemusic_session_user"
)
class SessionUserModel(
    @Id
    val id: UUID,
    @ManyToOne
    val deviceLink: UserDeviceLinkModel,
    @ManyToOne
    val session: QueueMusicSessionModel
) {
    companion object {
        fun new(deviceLink: UserDeviceLinkModel, session: QueueMusicSessionModel): SessionUserModel {
            return SessionUserModel(
                UUID.randomUUID(),
                deviceLink,
                session
            )
        }
    }
}