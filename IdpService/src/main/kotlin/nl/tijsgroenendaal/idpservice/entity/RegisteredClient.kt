package nl.tijsgroenendaal.idpservice.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id

@Entity
data class RegisteredClient(
    @Id
    val clientId: String,
    val clientSecret: String,
    val scopes: String?
)
