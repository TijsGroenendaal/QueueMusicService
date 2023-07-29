package nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.service

import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.clients.IdpClient
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.GenerateJwtCommand

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

    fun generateClientJwt(): String {
        val command = GenerateJwtCommand(
            spotifyFacadeClientId,
            spotifyFacadeClientSecret
        )

        return idpClient.generateClientJwt(command)
    }

}