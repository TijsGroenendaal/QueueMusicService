package nl.tijsgroenendaal.queuemusicservice.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class QueueMusicAuthentication(
    private val principal: QueueMusicPrincipalAuthentication,
    authorities: Collection<GrantedAuthority>
): AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): String {
        return name
    }

    override fun getPrincipal(): QueueMusicPrincipalAuthentication {
        return principal
    }
}