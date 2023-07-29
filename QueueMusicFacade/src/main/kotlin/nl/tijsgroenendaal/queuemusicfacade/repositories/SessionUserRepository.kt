package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionUserModel

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionUserRepository: JpaRepository<SessionUserModel, UUID> {
    fun deleteAllByUserAndSessionEndAtAfterAndSessionManualEnded(user: UUID, endDate: LocalDateTime = LocalDateTime.now(ZoneOffset.UTC), manualEnded: Boolean = false)
}