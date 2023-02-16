package nl.tijsgroenendaal.queuemusicservice.entity

import nl.tijsgroenendaal.queuemusicservice.commands.AddSessionSongCommand

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.UUID

@Entity(
    name = "queuemusic_session_song"
)
class SessionSong(
    @Id
    val id: UUID,
    @ManyToOne
    val deviceLink: UserDeviceLink,
    @ManyToOne
    val session: QueueMusicSession,
    val trackId: String?,
    val title: String,
    val album: String,
    val authors: String,
    val votes: Int,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun new(
            command: AddSessionSongCommand
        ): SessionSong {
            return SessionSong(
                UUID.randomUUID(),
                command.deviceLink,
                command.session,
                "",
                "",
                "",
                "",
                0,
                LocalDateTime.now(ZoneOffset.UTC)
            )
        }
    }
}