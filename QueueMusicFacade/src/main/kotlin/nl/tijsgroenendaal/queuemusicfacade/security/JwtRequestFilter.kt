package nl.tijsgroenendaal.queuemusicfacade.security

import nl.tijsgroenendaal.queuemusicfacade.helper.JwtTokenUtil
import nl.tijsgroenendaal.queuemusicfacade.helper.getUserIdFromSubject
import nl.tijsgroenendaal.queuemusicfacade.services.UserService
import nl.tijsgroenendaal.queuemusicfacade.security.model.QueueMusicAuthentication
import nl.tijsgroenendaal.queuemusicfacade.security.model.QueueMusicPrincipalAuthentication

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter

class JwtRequestFilter(
    private val userService: UserService,
    private val jwtTokenUtil: JwtTokenUtil
): OncePerRequestFilter() {

    private val excludedUri = arrayOf(
        "/v1/auth/login/**"
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
        return excludedUri.any { AntPathMatcher().match(it, request.requestURI) }
    }

    private fun getDeviceIdFromHeader(request: HttpServletRequest): String? {
        return request.getHeader("DeviceID")
    }
}