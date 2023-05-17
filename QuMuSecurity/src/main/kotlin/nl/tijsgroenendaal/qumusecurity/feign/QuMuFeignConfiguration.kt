package nl.tijsgroenendaal.qumusecurity.feign

import feign.RequestInterceptor
import feign.RequestTemplate
import nl.tijsgroenendaal.qumusecurity.security.JwtTokenUtil
import nl.tijsgroenendaal.qumusecurity.security.JwtTypes
import nl.tijsgroenendaal.qumusecurity.security.helper.getAuthenticationContext
import nl.tijsgroenendaal.qumusecurity.security.model.QueueMusicUserDetails
import org.springframework.context.annotation.Bean

class QuMuFeignConfiguration {
    @Bean
    fun feignRequestInterceptor(jwtTokenUtil: JwtTokenUtil) = FeignRequestInterceptor(jwtTokenUtil)
}

class FeignRequestInterceptor(
    private val jwtTokenUtil: JwtTokenUtil
) : RequestInterceptor {

    override fun apply(requestTemplate: RequestTemplate) {
        val authentication = getAuthenticationContext()
        val userDetails = QueueMusicUserDetails(
            id = authentication.principal.id,
            deviceId = authentication.principal.deviceId,
            authorities = authentication.principal.authorities,
        )

        val jwtToken = jwtTokenUtil.generateToken(userDetails, JwtTypes.ACCESS)
        requestTemplate.header("Authorization", "Bearer $jwtToken")
    }
}