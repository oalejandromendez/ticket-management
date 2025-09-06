package com.tickets.dto;

import lombok.*;

import java.time.Instant;

/**
 * DTO de autenticación que representa la información del usuario autenticado.
 * <p>
 * Contiene el token JWT, el nombre de usuario, el rol asignado y la fecha de expiración del token.
 * </p>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthDto {
    String token;
    String username;
    String role;
    Instant expiresAt;
}
