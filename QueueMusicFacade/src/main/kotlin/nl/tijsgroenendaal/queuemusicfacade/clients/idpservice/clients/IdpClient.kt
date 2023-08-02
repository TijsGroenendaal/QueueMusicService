package nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.clients

import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.responses.GenerateClientTokenCommandResponse
import nl.tijsgroenendaal.queuemusicfacade.clients.idpservice.command.GenerateClientTokenCommand
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${clients.idp-service}",
    name = "idp-service-client"
)
interface IdpClient {

    @PostMapping("/v1/secure/client-jwt")
    fun generateClientJwt(@RequestBody command: GenerateClientTokenCommand): GenerateClientTokenCommandResponse

}