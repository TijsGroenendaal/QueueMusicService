package nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.clients

import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.SpotifyTokenClientConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.RefreshedAccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.FormEncodedConfiguration
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.models.AccessTokenResponseModel
import nl.tijsgroenendaal.queuemusicservice.clients.spotify_client.configuration.FeignConfig

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping

@FeignClient(
    url = "\${clients.account-spotify-api}",
    name = "spotify-token-client",
    configuration = [SpotifyTokenClientConfiguration::class, FormEncodedConfiguration::class, FeignConfig::class]
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

}

data class RefreshTokenRequest(
    val refreshToken: String,
    val grantType: String = "refresh_token"
) {
    fun toForm(): Map<String, Any> {
        return mapOf(
            Pair("refresh_token", refreshToken),
            Pair("grant_type", grantType)
        )
    }
}

data class AccessTokenRequest(
    val code: String,
    val redirectUri: String = "https://www.google.com",
    val grantType: String = "authorization_code"
) {
    fun toForm(): Map<String, Any> {
        return mapOf(
            Pair("code", code),
            Pair("redirect_uri", redirectUri),
            Pair("grant_type", grantType)
        )
    }
}