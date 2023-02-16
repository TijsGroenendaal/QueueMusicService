package nl.tijsgroenendaal.queuemusicservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.util.UUID

@Entity(
    name = "user_device_link"
)
class UserDeviceLink(
    @Id
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val deviceId: String,
    @OneToOne
    val userModel: UserModel,
)