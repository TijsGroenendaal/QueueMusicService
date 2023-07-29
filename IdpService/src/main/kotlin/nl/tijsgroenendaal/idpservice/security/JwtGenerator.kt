package nl.tijsgroenendaal.idpservice.security

import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicClaims
import java.security.KeyFactory
import java.security.PrivateKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64

import java.util.Date

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class JwtGenerator(
    @Value("\${queuemusic.jwt.private-secret}")
    jwtSecret: String,
    @Value("\${queuemusic.jwt.refresh.private-secret}")
    refreshSecret: String
) {
    private val refreshJwtKey: PrivateKey
    private val jwtKey: PrivateKey

    init {
        val kf = KeyFactory.getInstance("RSA")
        val jwtKeySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(jwtSecret))
        jwtKey = kf.generatePrivate(jwtKeySpec)

        val refreshJwtKeySpec = PKCS8EncodedKeySpec(Base64.getDecoder().decode(refreshSecret))
        refreshJwtKey = kf.generatePrivate(refreshJwtKeySpec)
    }


    fun generateToken(userDetails: QueueMusicClaims, jwtType: JwtTypes): String {
        return when (jwtType) {
            JwtTypes.REFRESH -> generateRefreshToken(userDetails)
            JwtTypes.ACCESS -> generateAccessToken(userDetails)
        }
    }

    private fun generateAccessToken(claims: QueueMusicClaims): String {
        return doGenerateToken(
            claims,
            claims.subject,
            JWT_TOKEN_VALIDITY,
            jwtKey
        )
    }

    private fun generateRefreshToken(claims: QueueMusicClaims): String {
        return doGenerateToken(
            mapOf(),
            claims.subject,
            JWT_REFRESH_TOKEN_VALIDITY,
            refreshJwtKey
        )
    }

    private fun doGenerateToken(
        claims: Map<String, Any?>,
        subject: String,
        validity: Long,
        key: PrivateKey
    ): String {
        return Jwts.builder()
            .setClaims(claims.toMutableMap())
            .setSubject(subject)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + validity * 1000))
            .signWith(SignatureAlgorithm.RS256, key)
            .compact()
    }

    companion object {
        const val JWT_TOKEN_VALIDITY = (60 * 60 * 1 * 1).toLong() // = 3600 seconds = 1 hour
        const val JWT_REFRESH_TOKEN_VALIDITY = (60 * 60 * 24 * 90).toLong() // 90 days
    }
}