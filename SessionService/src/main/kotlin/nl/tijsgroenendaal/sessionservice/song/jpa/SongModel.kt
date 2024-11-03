package nl.tijsgroenendaal.sessionservice.song.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import nl.tijsgroenendaal.sessionservice.session.jpa.SessionModel
import nl.tijsgroenendaal.sessionservice.song.AddSongCommand
import java.time.Instant
import java.util.UUID

@Entity(
    name = "queuemusic_session_song"
)
class SongModel(
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
    val createdAt: Instant,
    @Enumerated(EnumType.STRING)
    var state: SongState
) {
    companion object {
        fun new(
            command: AddSongCommand
        ): SongModel {
            return SongModel(
                UUID.randomUUID(),
                command.userId,
                command.session,
                command.trackId,
                command.trackName,
                command.trackAlbum,
                command.trackArtists.joinToString(","),
                0,
                Instant.now(),
                SongState.QUEUED
            )
        }
    }
}