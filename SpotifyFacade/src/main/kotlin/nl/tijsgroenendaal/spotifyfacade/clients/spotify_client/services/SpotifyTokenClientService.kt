package nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.services

import nl.tijsgroenendaal.qumu.exceptions.AuthErrorCodes
import nl.tijsgroenendaal.qumu.exceptions.BadRequestException
import nl.tijsgroenendaal.qumu.exceptions.FallbackErrorCodes
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.clients.SpotifyTokenClient
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.AccessTokenQuery
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.CredentialsTokenQuery
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.RefreshTokenQuery
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.AccessTokenResponseModel
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.CredentialsTokenResponseModel
import nl.tijsgroenendaal.spotifyfacade.clients.spotify_client.query.responses.auth.RefreshedAccessTokenResponseModel

import feign.FeignException

import org.springframework.stereotype.Service

@Service
class SpotifyTokenClientService(
    private val spotifyTokenClient: SpotifyTokenClient,
) {

    fun getAccessToken(code: String, redirectUri: String): AccessTokenResponseModel {
        try {
            return spotifyTokenClient.getAccessToken(AccessTokenQuery(code, redirectUri).toForm())
        } catch(e: FeignException) {
            e.printStackTrace()
            if (e.status() == 400) {
                throw BadRequestException(AuthErrorCodes.UNABLE_TO_LOGIN_TO_LINK)
            }
            throw BadRequestException(FallbackErrorCodes.INTERNAL_SERVER_ERROR)
        }
    }

    fun getRefreshedAccessToken(refreshToken: String): RefreshedAccessTokenResponseModel =
        spotifyTokenClient.getRefreshAccessToken(RefreshTokenQuery(refreshToken).toForm())

    fun getCredentialsAccessToken(): CredentialsTokenResponseModel =
        spotifyTokenClient.getCredentialsToken(CredentialsTokenQuery().toForm())
}