package nl.tijsgroenendaal.queuemusicservice.repositories

import nl.tijsgroenendaal.queuemusicservice.entity.SessionSong

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface SessionSongRepository: JpaRepository<SessionSong, UUID> {

    fun countByDeviceLinkIdAndCreatedAtAfter(deviceLinkId: UUID, createdAt: LocalDateTime): Int

}