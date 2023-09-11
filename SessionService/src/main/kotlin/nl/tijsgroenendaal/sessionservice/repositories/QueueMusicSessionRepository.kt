package nl.tijsgroenendaal.sessionservice.repositories

import nl.tijsgroenendaal.sessionservice.entity.SessionModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface QueueMusicSessionRepository: PagingAndSortingRepository<SessionModel, UUID>, JpaRepository<SessionModel, UUID> {

    fun findAllByHostAndEndAtAfterAndManualEnded(hostId: UUID, endAt: LocalDateTime, manualEnded: Boolean = false): List<SessionModel>
    fun findByCode(code: String): SessionModel?

}