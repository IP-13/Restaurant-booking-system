package com.ip13.main.swagger

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {
    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .addSecurityItem(SecurityRequirement().addList("bearerAuth"))
        .components(
            Components()
                .addSecuritySchemes(
                    "bearerAuth",
                    SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                )
        )
        .info(
            Info().title("restaurant-service")
                .description("restaurant-service api")
                .version("1.0.0")
        )
}