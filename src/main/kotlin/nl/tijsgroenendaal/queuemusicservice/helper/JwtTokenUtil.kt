package nl.tijsgroenendaal.queuemusicservice.helper

import nl.tijsgroenendaal.queuemusicservice.security.JwtTypes

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

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

    fun getSubjectFromToken(token: String, jwtType: JwtTypes): String {
        return getClaimFromToken(token, jwtType, Claims::getSubject)
    }

    fun getExpirationDateFromToken(token: String, jwtType: JwtTypes): Date {
        return getClaimFromToken(token, jwtType, Claims::getExpiration)
    }

    private fun <T> getClaimFromToken(token: String, jwtType: JwtTypes, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token, jwtType)
        return claimsResolver.invoke(claims)
    }

    fun generateToken(userDetails: UserDetails, jwtType: JwtTypes): String {
        return when (jwtType) {
            JwtTypes.REFRESH -> generateRefreshToken(userDetails)
            JwtTypes.ACCESS -> generateAccessToken(userDetails)
        }
    }

    fun getAllClaimsFromToken(token: String, jwtType: JwtTypes): Claims {
        return when(jwtType) {
            JwtTypes.REFRESH -> Jwts.parser().setSigningKey(refreshSecret).parseClaimsJwt(token).body
            JwtTypes.ACCESS -> Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).body
        }
    }

    fun validateToken(token: String, jwtType: JwtTypes, userDetails: UserDetails): Boolean {
        val username = getSubjectFromToken(token, jwtType)
        return username == userDetails.username && !isTokenExpired(token, jwtType)
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

    private fun isTokenExpired(token: String, jwtType: JwtTypes): Boolean {
        val expiration = getExpirationDateFromToken(token, jwtType)
        return expiration.after(Date())
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = 60 * 60 * 1 * 0 // = 3600 seconds = 1 hour
        const val JWT_REFRESH_TOKEN_VALIDITY = 60 * 60 * 24 * 90 // 90 days
    }

}