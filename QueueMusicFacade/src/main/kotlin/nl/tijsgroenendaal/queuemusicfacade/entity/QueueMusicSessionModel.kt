package nl.tijsgroenendaal.queuemusicfacade.entity

import jakarta.persistence.*
import nl.tijsgroenendaal.queuemusicfacade.services.commands.CreateSessionCommand

import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Random
import java.util.UUID

private val ALLOWED_CODE_CHARS = ('A'..'Z') + ('a'..'z').toList()
private const val SESSION_CODE_LENGTH = 8

@Entity(
    name = "queuemusic_session"
)
class QueueMusicSessionModel(
    @Id
    val id: UUID,
    @ManyToOne
    val host: UserModel,
    val duration: Long,
    val createdAt: LocalDateTime,
    val endAt: LocalDateTime,
    val code: String,
    val playListId: String,
    @Column(name = "maximum_users")
    val maxUsers: Int,
    val manualEnded: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL], mappedBy = "session")
    val sessionUsers: MutableList<SessionUserModel> = mutableListOf()
) {
    companion object {
        fun new(command: CreateSessionCommand): QueueMusicSessionModel {
            return QueueMusicSessionModel(
                UUID.randomUUID(),
                command.userModel,
                command.duration,
                LocalDateTime.now(ZoneOffset.UTC),
                LocalDateTime.now(ZoneOffset.UTC).plusMinutes(command.duration),
                command.code,
                command.playlistId,
                command.maxUsers,
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
        return !this.endAt.isBefore(LocalDateTime.now(ZoneOffset.UTC))
    }

    fun hasRoom(): Boolean {
        return sessionUsers.size < maxUsers
    }

    fun hasJoined(id: UUID): Boolean {
        return sessionUsers.any { it.deviceLink.id == id }
    }
}