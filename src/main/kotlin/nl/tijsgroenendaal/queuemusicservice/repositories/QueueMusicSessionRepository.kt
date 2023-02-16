package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.entity.QueueMusicSession

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface QueueMusicSessionRepository: PagingAndSortingRepository<QueueMusicSession, UUID>, JpaRepository<QueueMusicSession, UUID> {

    fun findAllByHostIdAndEndAtAfterAndManualEnded(hostId: UUID, endAt: LocalDateTime, manualEnded: Boolean = false): List<QueueMusicSession>

}