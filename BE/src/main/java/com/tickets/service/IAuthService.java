package com.tickets.service;

import com.tickets.dto.AuthDto;
import com.tickets.dto.LoginDto;

/**
 * Interfaz que define los métodos de autenticación del sistema.
 * <p>
 * Implementada por {@link com.tickets.service.impl.AuthService} para manejar login y gestión de tokens.
 * </p>
 */
public interface IAuthService {

    /**
     * Realiza la autenticación de un usuario.
     *
     * @param loginDto DTO con el nombre de usuario y contraseña.
     * @return un {@link AuthDto} que contiene el token JWT, nombre de usuario, rol y fecha de expiración.
     */
    AuthDto login(LoginDto loginDto);
}