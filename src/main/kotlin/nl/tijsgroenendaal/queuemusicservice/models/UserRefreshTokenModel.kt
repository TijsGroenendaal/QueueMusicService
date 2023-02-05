package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.time.LocalDateTime
import java.util.UUID

@Entity
class UserRefreshTokenModel(
    @Id
    val id: UUID,
    @OneToOne
    val userModel: UserModel,
    val refreshToken: String?,
    val expireTime: LocalDateTime
)