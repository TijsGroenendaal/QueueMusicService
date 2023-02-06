package nl.tijsgroenendaal.queuemusicservice.models

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id

import java.util.UUID

@Entity(
    name = "user_device_link"
)
class UserDeviceLink(
    @Id
    val id: UUID,
    @Column(columnDefinition = "TEXT")
    val deviceId: String
)