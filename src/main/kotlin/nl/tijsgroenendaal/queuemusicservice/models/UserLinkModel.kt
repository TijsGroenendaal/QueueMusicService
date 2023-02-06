package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.time.LocalDateTime
import java.util.UUID

@Entity(
    name = "user_link"
)
class UserLinkModel(
    @Id
    val id: UUID,
    @OneToOne
    val userModel: UserModel,
    @Column(columnDefinition = "TEXT")
    val linkId: String,
    @Column(columnDefinition = "TEXT")
    var linkRefreshToken: String?,
    @Column(columnDefinition = "TEXT")
    var linkAccessToken: String?,
    var linkExpireTime: LocalDateTime
) {
    constructor(
        userModel: UserModel,
        linkId: String,
        linkRefreshToken: String?,
        linkAccessToken: String?,
        linkExpireTime: LocalDateTime
    ) : this(
        UUID.randomUUID(),
        userModel,
        linkId,
        linkRefreshToken,
        linkAccessToken,
        linkExpireTime
    )
}