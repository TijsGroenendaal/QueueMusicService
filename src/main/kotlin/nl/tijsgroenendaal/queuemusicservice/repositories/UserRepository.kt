package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.models.UserModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserRepository: JpaRepository<UserModel, UUID> {



}