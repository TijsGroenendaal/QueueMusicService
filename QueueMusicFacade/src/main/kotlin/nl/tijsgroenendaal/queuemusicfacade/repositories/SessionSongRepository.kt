package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongModel

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface SessionSongRepository: JpaRepository<SessionSongModel, UUID> {

    fun countByUserAndCreatedAtAfter(userId: UUID, createdAt: LocalDateTime): Int
    fun findAllBySessionId(sessionId: UUID): List<SessionSongModel>
}