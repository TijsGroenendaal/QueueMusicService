package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.util.UUID

@Entity(
    name = "user_model"
)
class UserModel(
    @Id
    val id: UUID,
    @OneToOne
    val userLink: UserLinkModel,
    @OneToOne(optional = true)
    val userRefreshToken: UserRefreshTokenModel
)