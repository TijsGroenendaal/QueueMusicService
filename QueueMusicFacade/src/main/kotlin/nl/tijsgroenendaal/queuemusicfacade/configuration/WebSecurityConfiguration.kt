package nl.tijsgroenendaal.queuemusicfacade.configuration

import nl.tijsgroenendaal.qumusecurity.security.JwtRequestFilter
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil

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
    private val jwtTokenUtil: JwtTokenUtil
) {

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeHttpRequests {
                it
                    .requestMatchers("/v1/auth/login/**")
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
            arrayOf("/v1/auth/login/**"))
    }

}