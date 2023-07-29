package nl.tijsgroenendaal.queuemusicfacade.entity

import nl.tijsgroenendaal.queuemusicfacade.commands.AddSessionSongCommand

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID
import jakarta.persistence.Column

@Entity(
    name = "queuemusic_session_song"
)
class SessionSongModel(
    @Id
    val id: UUID,
    @Column(name = "user_id")
    val user: UUID,
    @ManyToOne
    val session: SessionModel,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    var votes: Int,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun new(
            command: AddSessionSongCommand
        ): SessionSongModel {
            return SessionSongModel(
                UUID.randomUUID(),
                command.userId,
                command.session,
                command.trackId,
                command.trackName,
                command.trackAlbum,
                command.trackArtists,
                0,
                LocalDateTime.now(ZoneOffset.UTC)
            )
        }
    }
}