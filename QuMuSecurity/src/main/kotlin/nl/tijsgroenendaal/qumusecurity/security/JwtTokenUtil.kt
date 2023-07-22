package nl.tijsgroenendaal.qumusecurity.security

import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicClaims
import nl.tijsgroenendaal.qumu.exceptions.InvalidJwtException
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicAuthentication

import jakarta.servlet.http.HttpServletRequest

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

import java.util.Date

private const val REFRESH_URI = "/v1/auth/refresh"

@Component
class JwtTokenUtil(
    @Value("\${queuemusic.jwt.secret}")
    private val jwtSecret: String,
    @Value("\${queuemusic.jwt.refresh.secret}")
    private val refreshSecret: String
) {

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

    fun generateToken(userDetails: QueueMusicClaims, jwtType: JwtTypes): String {
        return when (jwtType) {
            JwtTypes.REFRESH -> generateRefreshToken(userDetails)
            JwtTypes.ACCESS -> generateAccessToken(userDetails)
        }
    }

    fun parseToken(token: String, jwtType: JwtTypes): Claims {
        return try {
            when(jwtType) {
                JwtTypes.REFRESH -> Jwts.parser().setSigningKey(refreshSecret).parseClaimsJws(token).body
                JwtTypes.ACCESS -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).body
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

    private fun generateAccessToken(claims: QueueMusicClaims): String {
        return doGenerateToken(
            claims,
            claims.subject,
            JWT_TOKEN_VALIDITY,
            jwtSecret
        )
    }

    private fun generateRefreshToken(claims: QueueMusicClaims): String {
        return doGenerateToken(
            mapOf(),
            claims.subject,
            JWT_REFRESH_TOKEN_VALIDITY,
            refreshSecret
        )
    }

    private fun doGenerateToken(
        claims: Map<String, Any?>,
        subject: String,
        validity: Long,
        secret: String
    ): String {
        return Jwts.builder()
            .setClaims(claims.toMutableMap())
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + validity * 1000))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = (60 * 60 * 1 * 1).toLong() // = 3600 seconds = 1 hour
        const val JWT_REFRESH_TOKEN_VALIDITY = (60 * 60 * 24 * 90).toLong() // 90 days
    }

}