package nl.tijsgroenendaal.spotifyfacade.entity;

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
    val userModelId: UUID,
    @Column(columnDefinition = "TEXT")
    val linkId: String,
    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor::class)
    var linkRefreshToken: String?,
    @Column(columnDefinition = "TEXT")
    @Convert(converter = AttributeEncryptor::class)
    var linkAccessToken: String?,
    var linkExpireTime: LocalDateTime
) {
    constructor(
        userId: UUID,
        linkId: String,
        linkRefreshToken: String?,
        linkAccessToken: String?,
        linkExpireTime: LocalDateTime
    ) : this(
        UUID.randomUUID(),
        userId,
        linkId,
        linkRefreshToken,
        linkAccessToken,
        linkExpireTime
    )
}