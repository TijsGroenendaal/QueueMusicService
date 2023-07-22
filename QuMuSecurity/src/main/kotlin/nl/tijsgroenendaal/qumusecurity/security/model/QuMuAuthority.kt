package nl.tijsgroenendaal.qumusecurity.security.model

import org.springframework.security.core.GrantedAuthority

class QuMuAuthority(
    private val authority: String
): GrantedAuthority {

    override fun getAuthority(): String {
        return authority
    }
}