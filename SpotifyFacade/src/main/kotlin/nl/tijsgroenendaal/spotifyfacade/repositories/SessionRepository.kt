package nl.tijsgroenendaal.spotifyfacade.repositories

import nl.tijsgroenendaal.spotifyfacade.entities.RootEntity

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.util.UUID

@Repository
interface SessionRepository: org.springframework.data.repository.Repository<RootEntity, UUID> {

    @Query("""
        SELECT
            QS.play_list_id
        FROM queuemusic_session AS QS
        WHERE QS.id = :sessionId
    """, nativeQuery = true)
    fun findPlaylistIdBySessionId(@Param("sessionId") sessionId: UUID): String?

}