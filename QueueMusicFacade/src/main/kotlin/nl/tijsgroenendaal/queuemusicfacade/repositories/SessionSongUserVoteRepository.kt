package nl.tijsgroenendaal.queuemusicfacade.repositories

import nl.tijsgroenendaal.queuemusicfacade.entity.SessionSongUserVoteModel
import java.util.UUID
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionSongUserVoteRepository: JpaRepository<SessionSongUserVoteModel, UUID> {

    fun findBySongIdAndUser(songId: UUID, userId: UUID): SessionSongUserVoteModel?

}