package nl.tijsgroenendaal.qumusecurity.security.model

import org.springframework.security.core.userdetails.UserDetails

import java.util.UUID

class QueueMusicUserDetails(
    val id: UUID,
    val deviceId: String?,
    val authorities: Set<Authorities>
): UserDetails {

    override fun getAuthorities(): Collection<Authorities> {
        return authorities
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return id.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }
}