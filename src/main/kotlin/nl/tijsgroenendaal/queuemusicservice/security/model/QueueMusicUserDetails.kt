package nl.tijsgroenendaal.queuemusicservice.security.model

import nl.tijsgroenendaal.queuemusicservice.entity.UserModel

import org.springframework.security.core.userdetails.UserDetails

import java.time.LocalDateTime
import java.time.ZoneOffset

class QueueMusicUserDetails(
    val userModel: UserModel,
    val authorities: Set<Authorities>
): UserDetails {

    override fun getAuthorities(): Collection<Authorities> {
        return authorities
    }

    override fun getPassword(): String {
        return ""
    }

    override fun getUsername(): String {
        return userModel.id.toString()
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        if (userModel.userLink == null) return false

        if (userModel.userLink!!.linkAccessToken != null &&
            userModel.userLink!!.linkExpireTime.isBefore(LocalDateTime.now(ZoneOffset.UTC)))
            return true

        return false
    }

    override fun isEnabled(): Boolean {
        return true
    }
}