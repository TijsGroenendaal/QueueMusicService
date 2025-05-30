package nl.tijsgroenendaal.sessionservice.session.jpa

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import nl.tijsgroenendaal.sessionservice.session.CreateSessionRequest
import nl.tijsgroenendaal.sessionservice.sessionuser.jpa.SessionUserModel
import java.time.Duration
import java.time.Instant
import java.util.Random
import java.util.UUID

private val ALLOWED_CODE_CHARS = ('A'..'Z') + ('a'..'z').toList()
private const val SESSION_CODE_LENGTH = 8

@Entity(
    name = "queuemusic_session"
)
class SessionModel(
    @Id
    val id: UUID,
    val host: UUID,
    val duration: Long,
    val createdAt: Instant,
    val endAt: Instant,
    val code: String,
    @Column(nullable = true)
    val autoplayAcceptance: Int?,
    @Column(name = "maximum_users")
    val maxUsers: Int,
    var manualEnded: Boolean = false,
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "session")
    val sessionUsers: MutableList<SessionUserModel> = mutableListOf()
) {
    companion object {
        fun new(command: CreateSessionRequest): SessionModel {
            return SessionModel(
                UUID.randomUUID(),
                command.userId,
                command.duration,
                Instant.now(),
                Instant.now().plus(Duration.ofMinutes(command.duration)),
                command.code,
                command.autoplayAcceptance,
                command.maxUsers,
            )
        }

        fun generateSessionCode(): String {
            val random = Random()
            val allowedCharSize = ALLOWED_CODE_CHARS.size - 1
            return String((0..SESSION_CODE_LENGTH).map { ALLOWED_CODE_CHARS[random.nextInt(allowedCharSize)] }
                .toCharArray())
        }
    }

    fun isActive(): Boolean {
        if (this.manualEnded)
            return false
        return !this.endAt.isBefore(Instant.now())
    }

    fun hasRoom(): Boolean {
        return sessionUsers.size < maxUsers
    }

    fun hasJoined(user: UUID): Boolean {
        return sessionUsers.any { it.user == user }
    }

    fun partOfSession(user: UUID): Boolean {
        return hasJoined(user) || isHost(user)
    }

    fun isHost(user: UUID): Boolean = this.host == user

    fun end() {
        this.manualEnded = true
    }
}