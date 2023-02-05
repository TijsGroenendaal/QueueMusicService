package nl.tijsgroenendaal.queuemusicservice.facades

import nl.tijsgroenendaal.queuemusicservice.services.UserService

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

import java.util.UUID

@Service
class JwtUserDetailsFacade(
    private val userService: UserService
): UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails {
        val id = try {
            UUID.fromString(username)
        } catch (e: IllegalArgumentException) {
            throw UsernameNotFoundException("User not found with id: $username ")
        }
        val user = userService.findByUsername(id)

        return User(user.id.toString(), "", mutableListOf())
    }
}