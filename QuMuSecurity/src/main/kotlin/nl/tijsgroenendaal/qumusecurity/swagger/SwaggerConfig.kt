package nl.tijsgroenendaal.qumusecurity.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


@Configuration
@EnableWebSecurity
class SwaggerConfig: WebMvcConfigurer {
	@Bean
	fun customOpenAPI(): OpenAPI {
		return OpenAPI()
			.components(
				Components()
					.addSecuritySchemes(
						"Authorization", SecurityScheme()
							.type(SecurityScheme.Type.APIKEY)
							.`in`(SecurityScheme.In.HEADER)
							.name("Authorization")
					)
			)
			.addSecurityItem(SecurityRequirement().addList("Authorization"))
	}

	override fun addViewControllers(registry: ViewControllerRegistry) {
		registry.addViewController("/").setViewName(
			"redirect:/swagger-ui/index.html"
		)
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	fun swaggerSecurity(http: HttpSecurity): SecurityFilterChain {
		http
			.securityMatcher("/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**")
			.authorizeHttpRequests {
				it.anyRequest().permitAll()
			}
		return http.build()
	}
}