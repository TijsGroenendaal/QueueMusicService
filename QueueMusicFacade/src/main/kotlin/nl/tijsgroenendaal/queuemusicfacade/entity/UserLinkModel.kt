package nl.tijsgroenendaal.queuemusicfacade.entity

import nl.tijsgroenendaal.qumusecurity.data.AttributeEncryptor

import jakarta.persistence.Column
import jakarta.persistence.Convert
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
    @Convert(converter = AttributeEncryptor::class)
    var linkRefreshToken: String?,
    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor::class)
    var linkAccessToken: String?,
    var linkExpireTime: LocalDateTime
)