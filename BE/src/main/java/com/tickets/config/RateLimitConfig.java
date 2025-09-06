package com.tickets.config;

import com.tickets.security.RateLimitFilter;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * Configuración de Rate Limiting para la aplicación.
 * <p>
 * Esta clase define las reglas para limitar la cantidad de solicitudes
 * que un usuario o IP puede hacer en un período determinado.
 * Esto ayuda a prevenir abuso de la API y ataques de denegación de servicio (DoS).
 * </p>
 */
@Configuration
public class RateLimitConfig {

    private final RateLimitFilter rateLimitFilter;

    @Value("${rate.limit.enabled:true}")
    private boolean rateLimitEnabled;

    /**
     * Configuración del rate limiting para la aplicación.
     * <p>
     * Inicializa el filtro de limitación de peticiones y establece si está habilitado.
     * </p>
     */
    public RateLimitConfig(RateLimitFilter rateLimitFilter) {
        this.rateLimitFilter = rateLimitFilter;
    }

    /**
     * Inicializa la configuración del rate limiting después de la construcción del bean.
     * <p>
     * Activa o desactiva el filtro de limitación de peticiones según la propiedad {@code rateLimitEnabled}.
     * </p>
     */
    @PostConstruct
    public void init() {
        rateLimitFilter.setEnabled(rateLimitEnabled);
    }
}