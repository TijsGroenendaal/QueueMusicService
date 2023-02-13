package nl.tijsgroenendaal.queuemusicservice.security

import nl.tijsgroenendaal.queuemusicservice.helper.JwtTokenUtil
import nl.tijsgroenendaal.queuemusicservice.helper.getUserIdFromSubject
import nl.tijsgroenendaal.queuemusicservice.services.UserService

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.annotation.Order

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Order(0)
@Component
class JwtRequestFilter(
    private val userService: UserService,
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
       val username = getUserIdFromSubject(jwtTokenUtil.getTokenFromRequest(request).subject)

        val userDetails = userService.findUserDetailsById(username)

        val authentication = QueueMusicAuthentication(
            QueueMusicPrincipalAuthentication(
                username,
                getDeviceIdFromHeader(request)
            ),
            userDetails.authorities)
            .apply {
                this.details = WebAuthenticationDetailsSource().buildDetails(request)
            }

        authentication.isAuthenticated = true

        SecurityContextHolder.getContext().authentication = authentication


        filterChain.doFilter(request, response)
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludedUri.contains(request.requestURI)
    }

    private fun getDeviceIdFromHeader(request: HttpServletRequest): String? {
        return request.getHeader("DeviceID")
    }
}