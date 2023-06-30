package nl.tijsgroenendaal.queuemusicfacade.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne

import java.util.UUID

@Entity(
    name = "user_model"
)
class UserModel(
    @Id
    val id: UUID,
    @OneToOne(optional = true, cascade = [CascadeType.ALL], mappedBy = "userModel")
    var userRefreshToken: UserRefreshTokenModel?,
    @OneToOne(optional = true, cascade = [CascadeType.ALL], mappedBy = "userModel")
    var userDeviceLink: UserDeviceLinkModel?
) {
    companion object {
        fun new(id: UUID): UserModel {
            return UserModel(id, null, null)
        }

        fun new(): UserModel {
            return UserModel(UUID.randomUUID(), null, null)
        }
    }
}