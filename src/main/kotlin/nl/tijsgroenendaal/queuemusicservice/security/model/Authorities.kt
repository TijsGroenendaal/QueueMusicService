package nl.tijsgroenendaal.queuemusicservice.security.model

import org.springframework.security.core.GrantedAuthority

enum class Authorities: GrantedAuthority {
    SPOTIFY,
    REFRESH;

    override fun getAuthority(): String {
        return this.name
    }
}