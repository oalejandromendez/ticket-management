package com.tickets.dto;

import lombok.*;

/**
 * DTO para la autenticación de usuarios.
 * <p>
 * Contiene el nombre de usuario y la contraseña para iniciar sesión en la aplicación.
 * </p>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {
    String username;
    String password;
}