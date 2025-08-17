package nl.tijsgroenendaal.sessionservice.song.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant

import java.util.UUID

@Repository
interface SongRepository : JpaRepository<SongModel, UUID> {

    fun countByUserAndCreatedAtAfter(userId: UUID, createdAt: Instant): Int

    fun findAllBySessionId(sessionId: UUID): List<SongModel>
}