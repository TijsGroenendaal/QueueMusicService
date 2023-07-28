package nl.tijsgroenendaal.qumusecurity.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class WebSecurityConfiguration(
    private val jwtTokenUtil: JwtTokenUtil,
    @Value("\${queuemusic.security.permitted}")
    private var permittedRequests: Array<String>
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests {
                it
                    .requestMatchers(*permittedRequests)
                    .permitAll()
                    .anyRequest()
                    .authenticated()
            }
            .addFilterBefore(getJwtFilter(), UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

    @Bean
    fun getJwtFilter(): JwtRequestFilter {
        return JwtRequestFilter(
            jwtTokenUtil,
            permittedRequests)
    }

}