package com.tickets.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.function.Function;

/**
 * Componente encargado de la generación y validación de tokens JWT.
 * Proporciona métodos para crear tokens, obtener información del usuario
 * desde el token y verificar la expiración del mismo.
 */
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    /**
     * Obtiene la clave secreta utilizada para firmar el token JWT.
     * Convierte la cadena secreta en un objeto Key compatible con HMAC-SHA.
     *
     * @return clave secreta como objeto Key
     */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Genera un token JWT para el usuario proporcionado.
     * Incluye el nombre de usuario como sujeto y los roles como claims.
     * Establece la fecha de emisión y la expiración del token.
     *
     * @param userDetails información del usuario para generar el token
     * @return token JWT como String
     */
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrae el nombre de usuario (subject) del token JWT proporcionado.
     *
     * @param token el token JWT a analizar
     * @return el nombre de usuario contenido en el token
     */
    public String extractUsername(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /**
     * Valida un token JWT verificando que coincida con el usuario y que no haya expirado.
     *
     * @param token       el token JWT a validar
     * @param userDetails los detalles del usuario a comparar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Obtiene la fecha y hora de expiración de un token JWT.
     *
     * @param token el token JWT
     * @return la fecha de expiración como un objeto Instant
     */
    public Instant getExpiration(String token) {
        return extractClaim(token, Claims::getExpiration).toInstant();
    }

    /**
     * Verifica si un token JWT ha expirado.
     *
     * @param token el token JWT a verificar
     * @return true si el token ha expirado, false en caso contrario
     */
    private boolean isTokenExpired(String token) {
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return expirationDate.before(new Date());
    }

    /**
     * Extrae un valor específico de las claims de un token JWT usando una función de resolución.
     *
     * @param token          el token JWT del cual extraer la claim
     * @param claimsResolver función que define qué claim extraer del JWT
     * @param <T>            tipo del valor a retornar
     * @return el valor de la claim extraída
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
