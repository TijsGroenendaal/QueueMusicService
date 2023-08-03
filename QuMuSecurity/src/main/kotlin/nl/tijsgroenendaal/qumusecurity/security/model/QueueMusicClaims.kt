package nl.tijsgroenendaal.qumusecurity.security.model

import nl.tijsgroenendaal.qumu.exceptions.AuthErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumusecurity.security.exceptions.InvalidUserIdException

import java.util.UUID

import io.jsonwebtoken.Claims
import io.jsonwebtoken.impl.DefaultClaims

import org.springframework.security.core.GrantedAuthority

private const val USER_ID = "uid"
private const val SCOPES = "scope"

class QueueMusicClaims : DefaultClaims {

    constructor(subject: String): super() {
        setSubject(subject)
    }
    constructor(claims: Claims): super(claims)

    fun getUserId(): UUID {
        if (!containsKey(USER_ID))
            throw BadRequestException(AuthErrorCodes.ANONYMOUS_CLIENT_CLAIMS)

        return getUserIdFromSubject(getString(USER_ID))
    }

    fun setUserId(userId: UUID) {
        put(USER_ID, userId)
    }

    fun getScope(): Set<QuMuAuthority> {
        if (!containsKey(SCOPES))
            return emptySet()

        return (get(SCOPES) as Collection<String>).map { QuMuAuthority(it) }.toSet()
    }

    fun setScope(scopes: Collection<GrantedAuthority>) {
        put(SCOPES, scopes.map { it.authority })
    }

    private fun getUserIdFromSubject(subject: String): UUID {
        return try {
            UUID.fromString(subject)
        } catch (e: IllegalArgumentException) {
            throw InvalidUserIdException("UserId is not a uuid")
        }
    }
}