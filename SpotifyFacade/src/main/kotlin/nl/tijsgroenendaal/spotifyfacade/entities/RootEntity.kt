package nl.tijsgroenendaal.spotifyfacade.entities

import jakarta.persistence.Entity
import jakarta.persistence.Id

import java.util.UUID

@Entity
data class RootEntity(
    @Id
    val id: UUID
)
