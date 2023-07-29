package nl.tijsgroenendaal.idpservice.facades

import nl.tijsgroenendaal.idpservice.commands.GenerateJwtCommand
import nl.tijsgroenendaal.idpservice.security.JwtGenerator
import nl.tijsgroenendaal.idpservice.services.RegisteredClientService
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.RegisteredClientErrorCodes
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.model.QuMuAuthority
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicClaims

import org.springframework.stereotype.Service

@Service
class JwtFacade(
    private val registeredClientService: RegisteredClientService,
    private val jwtGenerator: JwtGenerator
) {

    fun generateJwtForClient(command: GenerateJwtCommand): String {
        val client = registeredClientService.getById(command.clientId)

        if (client.clientSecret != command.clientSecret)
            throw BadRequestException(RegisteredClientErrorCodes.BAD_CLIENT_CREDENTIALS)

        val clientClaims = QueueMusicClaims(command.clientId)
        if (client.scopes != null) {
            clientClaims.setScope(client.scopes.split(",").map { QuMuAuthority(it) })
        }

        return jwtGenerator.generateToken(clientClaims, JwtTypes.ACCESS)
    }
}