package nl.tijsgroenendaal.queuemusicservice.security

import nl.tijsgroenendaal.queuemusicservice.facades.JwtUserDetailsFacade
import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

import java.util.UUID

@Component
class JwtRequestFilter(
    private val jwtUserDetailsFacade: JwtUserDetailsFacade,
    private val jwtTokenUtil: JwtTokenUtil
): OncePerRequestFilter() {

    private val excludedUri = arrayOf(
        "/v1/auth/login"
    )

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
       val username = jwtTokenUtil.getTokenFromHeader(request).body.subject

        val userDetails = jwtUserDetailsFacade.loadUserByUsername(username)

        val authentication = QueueMusicAuthentication(
            QueueMusicPrincipalAuthentication(
                userDetails.authorities
                    .filter { authority -> Authorities.values().map { it.name }.contains(authority.authority) }
                    .map { Authorities.valueOf(it.authority) },
                UUID.fromString(username)
            ),
            userDetails.authorities)
            .apply {
                this.details = WebAuthenticationDetailsSource().buildDetails(request)
            }

        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludedUri.contains(request.requestURI)
    }
}