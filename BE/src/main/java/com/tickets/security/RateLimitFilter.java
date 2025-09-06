package com.tickets.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Filtro para limitar la cantidad de solicitudes por cliente (IP) en un período de tiempo.
 * <p>
 * Extiende {@link OncePerRequestFilter} para garantizar que se ejecute una sola vez por solicitud.
 * Permite habilitar o deshabilitar el rate limiting mediante configuración.
 * </p>
 */
@Component
@Setter
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS = 60;
    private static final long WINDOW_SIZE = 60;

    private final Map<String, RequestCounter> requestCounts = new ConcurrentHashMap<>();

    private boolean enabled = true;

    /**
     * Método principal del filtro de limitación de solicitudes que se ejecuta por cada solicitud HTTP.
     * <p>
     * Verifica si la IP del cliente ha excedido el límite de solicitudes permitido y
     * bloquea la solicitud si se supera el límite.
     * </p>
     *
     * @param request     la solicitud HTTP entrante.
     * @param response    la respuesta HTTP.
     * @param filterChain el {@link FilterChain} para continuar con la ejecución de la solicitud.
     * @throws ServletException si ocurre un error en el filtrado.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        if (!enabled) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = request.getRemoteAddr();
        long now = Instant.now().getEpochSecond();

        RequestCounter counter = requestCounts.computeIfAbsent(clientIp, ip -> new RequestCounter(now, 0));

        synchronized (counter) {
            if (now - counter.timestamp >= WINDOW_SIZE) {
                counter.timestamp = now;
                counter.count = 0;
            }

            counter.count++;

            if (counter.count > MAX_REQUESTS) {
                response.setStatus(429);
                response.getWriter().write("Too many requests, please try again later.");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    /**
     * Clase interna que mantiene el conteo de solicitudes de una IP específica
     * y la marca de tiempo del primer registro.
     */
    private static class RequestCounter {
        long timestamp;
        int count;

        RequestCounter(long timestamp, int count) {
            this.timestamp = timestamp;
            this.count = count;
        }
    }
}