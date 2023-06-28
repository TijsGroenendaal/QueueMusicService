package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface QueueMusicSessionRepository: PagingAndSortingRepository<SessionModel, UUID>, JpaRepository<SessionModel, UUID> {

    fun findAllByHostIdAndEndAtAfterAndManualEnded(hostId: UUID, endAt: LocalDateTime, manualEnded: Boolean = false): List<SessionModel>
    fun findByCode(code: String): SessionModel?

}