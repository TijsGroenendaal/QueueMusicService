package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.QueueMusicSessionModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface QueueMusicSessionRepository: PagingAndSortingRepository<QueueMusicSessionModel, UUID>, JpaRepository<QueueMusicSessionModel, UUID> {

    fun findAllByHostIdAndEndAtAfterAndManualEnded(hostId: UUID, endAt: LocalDateTime, manualEnded: Boolean = false): List<QueueMusicSessionModel>
    fun findByCode(code: String): QueueMusicSessionModel?

}