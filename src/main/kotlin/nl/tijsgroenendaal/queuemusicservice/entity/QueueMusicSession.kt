package nl.tijsgroenendaal.queuemusicservice.entity

import nl.tijsgroenendaal.queuemusicservice.services.commands.CreateSessionCommand

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Random
import java.util.UUID

private val ALLOWED_CODE_CHARS = ('A'..'Z') + ('a'..'z').toList()
private const val SESSION_CODE_LENGTH = 8

@Entity(
    name = "queuemusic_session"
)
class QueueMusicSession(
    @Id
    val id: UUID,
    @ManyToOne
    val host: UserModel,
    val duration: Long,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String,
    val playListId: String,
    val manualEnded: Boolean = false,
) {
    companion object {
        fun new(command: CreateSessionCommand): QueueMusicSession {
            return QueueMusicSession(
                UUID.randomUUID(),
                command.userModel,
                command.duration,
                LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC).plusMinutes(command.duration),
                command.code,
                command.playlistId
            )
        }

        fun generateSessionCode(): String {
            val random = Random()
            val allowedCharSize = ALLOWED_CODE_CHARS.size - 1
            return String((0..SESSION_CODE_LENGTH).map { ALLOWED_CODE_CHARS[random.nextInt(allowedCharSize)] }.toCharArray())
        }
    }

    fun isActive(): Boolean {
        if (this.manualEnded)
            return false
        if (this.endAt.isBefore(LocalDateTime.now(ZoneOffset.UTC)))
            return false
        return true
    }
}