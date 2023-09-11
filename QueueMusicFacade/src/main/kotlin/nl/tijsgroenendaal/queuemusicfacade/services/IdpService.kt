package nl.tijsgroenendaal.queuemusicfacade.services

import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.clients.IdpClient
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.GenerateClientTokenCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.responses.GenerateClientTokenCommandResponse

import org.springframework.stereotype.Service

@Service
class IdpService(
    private val idpClient: IdpClient,

) {
    fun generateClientJwt(clientId: String, clientSecret: String): GenerateClientTokenCommandResponse {
        val command = GenerateClientTokenCommand(clientId, clientSecret)
        return idpClient.generateClientJwt(command)
    }
}