package nl.tijsgroenendaal.queuemusicfacade.entity

import jakarta.persistence.*
import nl.tijsgroenendaal.queuemusicfacade.helper.AttributeEncryptor

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
    @Convert(converter = AttributeEncryptor::class)
    var refreshToken: String,
    var expireTime: LocalDateTime
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