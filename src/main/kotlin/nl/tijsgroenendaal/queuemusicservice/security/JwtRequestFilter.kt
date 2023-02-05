package nl.tijsgroenendaal.queuemusicservice.security

import nl.tijsgroenendaal.queuemusicservice.exceptions.UnAuthenticatedException
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

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authenticationHeader = request.getHeader("Authorization")

        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            val jwtToken = authenticationHeader.substring(7)
            val username = try {
                 jwtTokenUtil.getSubjectFromToken(jwtToken)
            } catch (e: Exception) {
                throw UnAuthenticatedException()
            }

            val userDetails = jwtUserDetailsFacade.loadUserByUsername(username)
            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {
                val authentication = QueueMusicAuthentication(
                    QueueMusicPrincipalAuthentication(
                        arrayOf(),
                        UUID.fromString(username)
                    ),
                    arrayListOf()
                ).apply {
                    this.details = WebAuthenticationDetailsSource().buildDetails(request)
                }

                SecurityContextHolder.getContext().authentication = authentication
            }

            filterChain.doFilter(request, response)
        }
    }
}