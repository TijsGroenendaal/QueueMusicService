package nl.tijsgroenendaal.qumusecurity.feign

import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContext

import feign.RequestInterceptor
import feign.RequestTemplate

import org.springframework.context.annotation.Bean

class QuMuFeignConfiguration {
    @Bean
    fun feignRequestInterceptor(jwtTokenUtil: JwtTokenUtil) = FeignRequestInterceptor(jwtTokenUtil)
}

class FeignRequestInterceptor(
    private val jwtTokenUtil: JwtTokenUtil
) : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val authentication = getAuthenticationContext().claims
        val jwtToken = jwtTokenUtil.generateToken(authentication, JwtTypes.ACCESS)
        requestTemplate.header("Authorization", "Bearer $jwtToken")
    }
}