package nl.tijsgroenendaal.qumusecurity.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.AntPathMatcher
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver

class JwtRequestFilter(
    private val jwtTokenUtil: JwtTokenUtil,
    private val excludedUri: Array<String>,
    private val exceptionResolver: HandlerExceptionResolver
): OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            val authentication = jwtTokenUtil.getAuthenticationFromRequest(request)

            SecurityContextHolder.getContext().authentication = authentication
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            exceptionResolver.resolveException(request, response, null, e)
        }
    }

    override fun shouldNotFilter(request: HttpServletRequest): Boolean {
        return excludedUri.any { AntPathMatcher().match(it, request.requestURI) }
    }
}