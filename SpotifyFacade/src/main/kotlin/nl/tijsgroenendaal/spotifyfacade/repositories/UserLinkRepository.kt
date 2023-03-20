package nl.tijsgroenendaal.spotifyfacade.repositories

import nl.tijsgroenendaal.spotifyfacade.entities.RootEntity
import nl.tijsgroenendaal.spotifyfacade.repositories.models.UserLinkModel

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

import java.time.LocalDateTime
import java.util.UUID

@Repository
interface UserLinkRepository: org.springframework.data.repository.Repository<RootEntity, UUID> {

    @Query("""
        SELECT
            UL.id,
            UL.link_access_token,
            UL.link_expire_time,
            UL.link_id,
            UL.link_refresh_token
        FROM queuemusic_session AS QS
        LEFT JOIN user_link AS UL ON UL.user_model_id = QS.host
        WHERE QS.id = :sessionId
    """, nativeQuery = true)
    fun findUserLinkBySessionHost(@Param("sessionId") sessionId: UUID): UserLinkModel?

    @Modifying
    @Query("""
        UPDATE user_link
        SET 
            link_access_token = :access_token,
            link_expire_time = :expire_time
        WHERE 
            user_link.id = :link_id
    """, nativeQuery = true)
    fun updateAccessTokens(@Param("link_id") linkId: UUID, @Param("access_token") accessToken: String, @Param("expire_time") expireTime: LocalDateTime)

}