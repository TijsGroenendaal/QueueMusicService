package nl.tijsgroenendaal.sessionservice.sessionuser.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import java.util.UUID

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