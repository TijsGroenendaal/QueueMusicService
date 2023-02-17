package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyTokenClientConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.FormEncodedConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.query.responses.auth.CredentialsTokenResponseModel

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    url = "\${clients.account-spotify-api}",
    name = "spotify-token-client",
    configuration = [SpotifyTokenClientConfiguration::class, FormEncodedConfiguration::class]
)
interface SpotifyTokenClient {

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getRefreshAccessToken(
        form: Map<String, Any>
    ): RefreshedAccessTokenResponseModel

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getAccessToken(
        form: Map<String, Any>
    ): AccessTokenResponseModel


    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getCredentialsToken(
        form: Map<String, Any>
    ): CredentialsTokenResponseModel

}