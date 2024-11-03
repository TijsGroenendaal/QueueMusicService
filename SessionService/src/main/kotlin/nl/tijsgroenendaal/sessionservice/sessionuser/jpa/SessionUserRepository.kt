package nl.tijsgroenendaal.sessionservice.sessionuser.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface SessionUserRepository : JpaRepository<SessionUserModel, UUID> {
    fun deleteAllByUserAndSessionEndAtAfterAndSessionManualEnded(
        user: UUID,
        endDate: Instant = Instant.now(),
        manualEnded: Boolean = false
    )
}