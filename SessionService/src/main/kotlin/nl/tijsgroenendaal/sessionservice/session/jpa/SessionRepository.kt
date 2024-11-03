package nl.tijsgroenendaal.sessionservice.session.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.Instant
import java.util.UUID

@Repository
interface SessionRepository : PagingAndSortingRepository<SessionModel, UUID>, JpaRepository<SessionModel, UUID> {

    fun findAllByHostAndEndAtAfterAndManualEnded(
        hostId: UUID,
        endAt: Instant = Instant.now(),
        manualEnded: Boolean = false
    ): List<SessionModel>

    fun findByCode(code: String): SessionModel?

    @Query(
        """
        SELECT su.session
        FROM queuemusic_session_user su
        WHERE su.user = :user
        AND su.session.endAt < now()
        AND su.session.manualEnded = false
    """
    )
    fun findByUser(user: UUID): SessionModel?

}