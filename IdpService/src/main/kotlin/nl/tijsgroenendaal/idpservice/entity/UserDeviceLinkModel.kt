package nl.tijsgroenendaal.idpservice.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.util.UUID

@Entity(
    name = "user_device_link"
)
class UserDeviceLinkModel(
    @Id
    val id: UUID,
    @Column(columnDefinition = "TEXT", unique = true)
    var deviceId: String,
    @OneToOne(cascade = [CascadeType.ALL])
    var user: UserModel,
) {
    companion object {
        fun new(deviceId: String, userModel: UserModel): UserDeviceLinkModel {
            return UserDeviceLinkModel(
                UUID.randomUUID(),
                deviceId,
                userModel
            )
        }
    }
}