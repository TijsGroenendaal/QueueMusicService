package nl.tijsgroenendaal.spotifyfacade.repositories

import nl.tijsgroenendaal.spotifyfacade.entity.UserLinkModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface UserLinkRepository: JpaRepository<UserLinkModel, UUID> {

    fun findByUserModelId(userId: UUID): UserLinkModel?

}