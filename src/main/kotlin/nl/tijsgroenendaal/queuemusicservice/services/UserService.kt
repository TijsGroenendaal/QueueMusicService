package nl.tijsgroenendaal.queuemusicservice.services

import nl.tijsgroenendaal.queuemusicservice.exceptions.NotFoundException
import nl.tijsgroenendaal.queuemusicservice.models.UserModel
import nl.tijsgroenendaal.queuemusicservice.repositories.UserRepository

import org.springframework.security.core.userdetails.UsernameNotFoundException

import org.springframework.stereotype.Service
import java.util.UUID

@Service
class UserService(
    private val userRepository: UserRepository
) {

    @Throws(UsernameNotFoundException::class)
    fun findByUsername(username: UUID): UserModel {
        return userRepository
            .findById(username)
            .let {
                if (it.isEmpty) throw UsernameNotFoundException("User not found with id: $username")
                it.get()
            }
    }

    fun findById(id: UUID): UserModel {
        return userRepository
            .findById(id)
            .let {
                if(it.isEmpty) throw NotFoundException(id.toString())
                it.get()
            }
    }

}