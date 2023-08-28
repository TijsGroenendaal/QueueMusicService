package nl.tijsgroenendaal.sessionservice.repositories

import nl.tijsgroenendaal.sessionservice.entity.SessionSongUserVoteModel

import java.util.UUID

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SessionSongUserVoteRepository: JpaRepository<SessionSongUserVoteModel, UUID> {

    fun findBySongIdAndUser(songId: UUID, userId: UUID): SessionSongUserVoteModel?

}