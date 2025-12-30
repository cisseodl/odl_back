package com.odc.aws_learning.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "Bearer Authentication";
        
        return new OpenAPI()
                .info(new Info()
                        .title("ODL Learning API")
                        .version("1.0")
                        .description("API de gestion de la plateforme de formation"))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, createAPIKeyScheme()))
                // Note: SecurityRequirement n'est pas appliqué globalement ici
                // pour permettre au bouton "Authorize" d'apparaître dans Swagger UI
                // Les endpoints protégés utiliseront @Operation(security = @SecurityRequirement(...))
                // ou seront automatiquement détectés via @PreAuthorize
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName));
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }
}
