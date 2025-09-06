package com.tickets.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Swagger / OpenAPI para la aplicación.
 * <p>
 * Esta clase permite la generación automática de documentación
 * de la API REST, incluyendo los endpoints, modelos y descripciones.
 * Esto facilita a los desarrolladores y testers explorar y probar la API.
 * </p>
 */
@Configuration
public class SwaggerConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    /**
     * Configura la documentación OpenAPI (Swagger) para la aplicación.
     * <p>
     * Define el título, versión, esquema de seguridad y credenciales JWT para la API.
     * </p>
     *
     * @return un objeto {@link OpenAPI} configurado para la documentación de la API.
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Ticket Management API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components()
                        .addSecuritySchemes(SECURITY_SCHEME_NAME,
                                new SecurityScheme()
                                        .name("Authorization")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                );
    }
}
