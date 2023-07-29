package nl.tijsgroenendaal.queuemusicfacade.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.util.UUID
import jakarta.persistence.Column

@Entity(
    name = "queuemusic_session_user"
)
class SessionUserModel(
    @Id
    val id: UUID,
    @Column(name = "user_id")
    val user: UUID,
    @ManyToOne
    val session: SessionModel
) {
    companion object {
        fun new(userId: UUID, session: SessionModel): SessionUserModel {
            return SessionUserModel(
                UUID.randomUUID(),
                userId,
                session
            )
        }
    }
}