package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.time.LocalDateTime
import java.util.UUID

@Entity(
    name = "user_refresh_token"
)
class UserRefreshTokenModel(
    @Id
    val id: UUID,
    @OneToOne
    val userModel: UserModel,
    @Column(columnDefinition = "TEXT")
    val refreshToken: String?,
    val expireTime: LocalDateTime
) {
    constructor(
        userModel: UserModel,
        refreshToken: String,
        expireTime: LocalDateTime
    ): this(
        UUID.randomUUID(),
        userModel,
        refreshToken,
        expireTime
    )
}