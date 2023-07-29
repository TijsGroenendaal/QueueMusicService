package nl.tijsgroenendaal.idpservice.repositories

import nl.tijsgroenendaal.idpservice.entity.UserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserModel, UUID> {
}