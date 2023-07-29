package nl.tijsgroenendaal.qumusecurity.security

import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicClaims
import nl.tijsgroenendaal.qumu.exceptions.InvalidJwtException
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication

import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.X509EncodedKeySpec
import java.util.Base64

import jakarta.servlet.http.HttpServletRequest

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

private const val REFRESH_URI = "/v1/auth/refresh"

@Component
class JwtTokenUtil(
    @Value("\${queuemusic.jwt.public-secret}")
    jwtPublicSecret: String,
    @Value("\${queuemusic.jwt.refresh.public-secret}")
    refreshPublicSecret: String
) {

    private val jwtPublicKey: PublicKey
    private val refreshJwtPublicKey: PublicKey

    init {
        val kf = KeyFactory.getInstance("RSA")
        val jwtPublicKeySpec = X509EncodedKeySpec(Base64.getDecoder().decode(jwtPublicSecret))
        jwtPublicKey = kf.generatePublic(jwtPublicKeySpec)

        val refreshJwtPublicKeySpec = X509EncodedKeySpec(Base64.getDecoder().decode(refreshPublicSecret))
        refreshJwtPublicKey = kf.generatePublic(refreshJwtPublicKeySpec)
    }


    fun getAuthenticationFromRequest(request: HttpServletRequest): QueueMusicAuthentication {
        val authenticationHeader = request.getHeader("Authorization")

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw InvalidJwtException()
        }

        val jwtType = if (request.requestURI == REFRESH_URI) JwtTypes.REFRESH else JwtTypes.ACCESS

        return getAuthenticationFromClaims(parseToken(getTokenFromHeader(authenticationHeader), jwtType))
    }

    fun getTokenFromHeader(header: String): String {
        return header.substring(7)
    }

    fun parseToken(token: String, jwtType: JwtTypes): Claims {
        return try {
            when(jwtType) {
                JwtTypes.REFRESH -> Jwts.parser().setSigningKey(refreshJwtPublicKey).parseClaimsJws(token).body
                JwtTypes.ACCESS -> Jwts.parser().setSigningKey(jwtPublicKey).parseClaimsJws(token).body
            }
        } catch (e: Exception) {
            when(e) {
                is MalformedJwtException -> throw InvalidJwtException()
                is IllegalArgumentException -> throw InvalidJwtException()
                is SignatureException -> throw InvalidJwtException()
                is UnsupportedJwtException -> throw InvalidJwtException()
                else -> throw e
            }
        }
    }

    private fun getAuthenticationFromClaims(claims: Claims): QueueMusicAuthentication = QueueMusicAuthentication(QueueMusicClaims(claims))

}