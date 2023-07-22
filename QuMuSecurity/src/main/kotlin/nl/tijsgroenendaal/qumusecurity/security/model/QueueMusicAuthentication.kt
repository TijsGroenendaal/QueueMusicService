package nl.tijsgroenendaal.qumusecurity.security.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority

class QueueMusicAuthentication(
    val claims: QueueMusicClaims
): Authentication {

    override fun getName(): String {
        return claims.subject
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return claims.getScope()
    }

    override fun getCredentials(): Any {
        throw NotImplementedError()
    }

    override fun getDetails(): Any {
        throw NotImplementedError()
    }

    override fun getPrincipal(): QueueMusicClaims {
        return claims
    }

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) { }
}