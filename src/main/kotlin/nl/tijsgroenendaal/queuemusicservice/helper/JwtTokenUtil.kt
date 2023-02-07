package nl.tijsgroenendaal.queuemusicservice.helper

import nl.tijsgroenendaal.queuemusicservice.security.JwtTypes
import nl.tijsgroenendaal.queuemusicservice.exceptions.InvalidJwtException

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Header
import io.jsonwebtoken.Jwt
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.SignatureException
import io.jsonwebtoken.UnsupportedJwtException

import jakarta.servlet.http.HttpServletRequest

import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component

import java.io.Serializable
import java.util.Date

@Component
class JwtTokenUtil(
    @Value("\${queuemusic.jwt.secret}")
    private val jwtSecret: String,
    @Value("\${queuemusic.jwt.refresh.secret}")
    private val refreshSecret: String
): Serializable {

    private val refreshUri = "/v1/auth/refresh"

    fun getTokenFromHeader(request: HttpServletRequest): Jwt<Header<*>, Claims> {
        val authenticationHeader = request.getHeader("Authorization")

        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            throw InvalidJwtException()
        }

        val jwtType = if (request.requestURI == refreshUri) JwtTypes.REFRESH else JwtTypes.ACCESS

        val jwtToken = authenticationHeader.substring(7)
        return parseToken(jwtToken, jwtType)
    }

    fun generateToken(userDetails: UserDetails, jwtType: JwtTypes): String {
        return when (jwtType) {
            JwtTypes.REFRESH -> generateRefreshToken(userDetails)
            JwtTypes.ACCESS -> generateAccessToken(userDetails)
        }
    }

    fun parseToken(token: String, jwtType: JwtTypes): Jwt<Header<*>, Claims> {
        return try {
            when(jwtType) {
                JwtTypes.REFRESH -> Jwts.parser().setSigningKey(refreshSecret).parseClaimsJwt(token)
                JwtTypes.ACCESS -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token)
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

    private fun generateAccessToken(userDetails: UserDetails): String {
        val claims = mapOf<String, Any>(
            Pair("subs", userDetails.authorities.map { it.authority })
        )
        return doGenerateToken(
            claims,
            userDetails.username,
            JWT_TOKEN_VALIDITY,
            jwtSecret
        )
    }

    private fun generateRefreshToken(userDetails: UserDetails): String {
        return doGenerateToken(
            mapOf(),
            userDetails.username,
            JWT_REFRESH_TOKEN_VALIDITY,
            refreshSecret
        )
    }


    private fun doGenerateToken(
        claims: Map<String, Any>,
        subject: String,
        validity: Int,
        secret: String
    ): String {
        JWT_TOKEN_VALIDITY

        return Jwts.builder()
            .setClaims(claims.toMutableMap())
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + validity * 1000))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact()
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = 60 * 60 * 1 * 0 // = 3600 seconds = 1 hour
        const val JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 90 // 90 days
    }

}