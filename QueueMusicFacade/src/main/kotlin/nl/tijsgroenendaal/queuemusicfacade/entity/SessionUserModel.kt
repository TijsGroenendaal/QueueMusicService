package nl.tijsgroenendaal.queuemusicfacade.entity

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
    val user: UserModel,
    @ManyToOne
    val session: SessionModel
) {
    companion object {
        fun new(user: UserModel, session: SessionModel): SessionUserModel {
            return SessionUserModel(
                UUID.randomUUID(),
                user,
                session
            )
        }
    }
}