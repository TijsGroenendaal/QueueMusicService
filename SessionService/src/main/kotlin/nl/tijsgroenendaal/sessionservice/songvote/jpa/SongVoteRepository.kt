package nl.tijsgroenendaal.sessionservice.songvote.jpa

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface SongVoteRepository : JpaRepository<SongVoteModel, UUID> {

    fun findBySongIdAndUser(songId: UUID, userId: UUID): SongVoteModel?
    fun findBySongId(song: UUID): List<SongVoteModel>

}