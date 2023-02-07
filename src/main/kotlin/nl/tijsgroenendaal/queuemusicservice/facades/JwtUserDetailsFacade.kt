package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.helper.getUserIdFromSubject
import nl.tijsgroenendaal.queuemusicservice.security.Authorities
import nl.tijsgroenendaal.queuemusicservice.services.UserService

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class JwtUserDetailsFacade(
    private val userService: UserService
): UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val id = getUserIdFromSubject(id)
        val user = userService.findByUsername(id)

        val authorities = mutableListOf<Authorities>()
        if (user.userLink != null) {
            authorities.add(Authorities.SPOTIFY)
        }

        return User.builder()
            .username(user.id.toString())
            .password("")
            .authorities(*authorities.map { it.name }.toTypedArray())
            .build()
    }
}