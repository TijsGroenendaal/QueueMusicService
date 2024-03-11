package nl.tijsgroenendaal.qumusecurity.security

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.HandlerExceptionResolver

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
    private val jwtTokenUtil: JwtTokenUtil,
    @Value("\${queuemusic.security.permitted}")
    private var permittedRequests: Array<String>,
    @Qualifier("handlerExceptionResolver")
    private val exceptionResolver: HandlerExceptionResolver
) {

    @Bean
    fun webSecurityCustomizer() = WebSecurityCustomizer {
        web: WebSecurity -> web.ignoring().requestMatchers(
        "/",
        "/error",
        "/favicon.ico",
        "/*.html",
        "/*/*.html",
        "/*/*.css",
        "/*/*.js",
        )
    }

    @Bean
    @Order(Ordered.LOWEST_PRECEDENCE)
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .authorizeHttpRequests {
                it.anyRequest().authenticated()
            }
            .csrf {
                it.disable()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .headers {
                it.cacheControl { }
            }
            .addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    fun getJwtFilter(): JwtRequestFilter {
        return JwtRequestFilter(
            jwtTokenUtil,
            permittedRequests,
            exceptionResolver)
    }

}