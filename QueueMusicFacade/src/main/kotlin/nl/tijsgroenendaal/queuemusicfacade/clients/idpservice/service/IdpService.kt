package nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.service

import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.clients.IdpClient
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.GenerateClientTokenCommand
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.responses.GenerateClientTokenCommandResponse

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class IdpService(
    private val idpClient: IdpClient,
    @Value("\${clients.idp.spotify-facade.id}")
    private val spotifyFacadeClientId: String,
    @Value("\${clients.idp.spotify-facade.secret}")
    private val spotifyFacadeClientSecret: String
) {

    fun generateClientJwt(): GenerateClientTokenCommandResponse {
        val command = GenerateClientTokenCommand(
            spotifyFacadeClientId,
            spotifyFacadeClientSecret
        )

        return idpClient.generateClientJwt(command)
    }

}