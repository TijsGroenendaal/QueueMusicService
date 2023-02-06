package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyTokenClientConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.FormEncodedConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.AccessTokenModel

import com.fasterxml.jackson.annotation.JsonProperty

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

@FeignClient(
    url = "\${clients.spotify-api}/v1/token",
    name = "spotify-token-client",
    configuration = [SpotifyTokenClientConfiguration::class, FormEncodedConfiguration::class]
)
interface SpotifyTokenClient {

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getRefreshAccessToken(
        @RequestBody tokenRequest: RefreshTokenRequest
    ): RefreshedAccessTokenModel

    @PostMapping(consumes = [MediaType.APPLICATION_FORM_URLENCODED_VALUE])
    fun getAccessToken(
        @RequestBody tokenRequest: AccessTokenRequest
    ): AccessTokenModel

}

data class RefreshTokenRequest(
    @JsonProperty("refresh_token")
    val refreshToken: String,
    @JsonProperty("grant_type")
    val grantType: String = "refresh_token"
)

data class AccessTokenRequest(
    @JsonProperty("code")
    val code: String,
    @JsonProperty("redirect_uri")
    val redirectUri: String = "https://www.google.com",
    @JsonProperty("grant_type")
    val grantType: String = "authorization_code"
)