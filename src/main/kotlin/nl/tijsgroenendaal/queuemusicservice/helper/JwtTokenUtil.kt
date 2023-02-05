package nl.tijsgroenendaal.queuemusicservice.helper

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
    private val jwtSecret: String
): Serializable {

    fun getSubjectFromToken(token: String): String {
        return getClaimFromToken(token, Claims::getSubject)
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Claims::getExpiration)
    }

    private fun <T> getClaimFromToken(token: String, claimsResolver: (Claims) -> T): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.invoke(claims)
    }

    fun generateToken(userDetails: UserDetails): String {
        val claims = mapOf<String, Any>()
        return doGenerateToken(claims, userDetails.username)
    }

    fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJwt(token).body
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val username = getSubjectFromToken(token)
        return username == userDetails.username && !isTokenExpired(token)
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
            .signWith(SignatureAlgorithm.HS256, jwtSecret)
            .compact()
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.after(Date())
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = 1  * 60 * 60 // = 3600 seconds = 1 hour
    }

}