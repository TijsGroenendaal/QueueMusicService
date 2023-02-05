package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.models.UserLinkModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserLinkRepository: JpaRepository<UserLinkModel, UUID> {

    fun findByUserModelId(userId: UUID): UserLinkModel?

}