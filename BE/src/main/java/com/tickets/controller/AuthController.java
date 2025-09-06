package com.tickets.controller;

import com.tickets.dto.AuthDto;
import com.tickets.dto.LoginDto;
import com.tickets.service.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de autenticación para la aplicación.
 * <p>
 * Este controlador maneja las operaciones relacionadas con la autenticación de usuarios,
 * como el login. Los endpoints expuestos son accesibles sin autenticación previa.
 * </p>
 */
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final IAuthService iAuthService;

    /**
     * Endpoint para iniciar sesión en la aplicación.
     * <p>
     * Recibe un {@link LoginDto} con nombre de usuario y contraseña,
     * y devuelve un {@link AuthDto} con el token JWT y detalles del usuario.
     * </p>
     *
     * @param loginDto los datos de autenticación del usuario.
     * @return un {@link ResponseEntity} con el {@link AuthDto} y código 200 OK.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthDto> login(@RequestBody LoginDto loginDto) {
        return ResponseEntity.ok(iAuthService.login(loginDto));
    }
}
