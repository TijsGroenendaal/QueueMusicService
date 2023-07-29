package nl.tijsgroenendaal.idpservice.services

import nl.tijsgroenendaal.idpservice.entity.RegisteredClient
import nl.tijsgroenendaal.idpservice.repositories.RegisteredClientRepository
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.RegisteredClientErrorCodes
import org.springframework.stereotype.Service

@Service
class RegisteredClientService(
    private val registeredClientRepository: RegisteredClientRepository
) {
    fun getById(clientId: String): RegisteredClient = registeredClientRepository.findById(clientId)
        .orElseThrow { BadRequestException(RegisteredClientErrorCodes.CLIENT_NOT_FOUND) }
}