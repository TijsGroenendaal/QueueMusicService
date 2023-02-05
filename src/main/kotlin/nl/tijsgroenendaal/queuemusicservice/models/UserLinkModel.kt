package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.time.LocalDateTime

@Entity(
    name = "user_link"
)
class UserLinkModel(
    @Id
    val id: Long,
    @OneToOne
    val userModel: UserModel,
    val linkId: String,
    var linkRefreshToken: String?,
    var linkAccessToken: String?,
    var linkExpireTime: LocalDateTime
)