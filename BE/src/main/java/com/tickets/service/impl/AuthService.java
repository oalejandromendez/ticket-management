package com.tickets.service.impl;

import com.tickets.dto.AuthDto;
import com.tickets.dto.LoginDto;
import com.tickets.service.IAuthService;
import com.tickets.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * Servicio de autenticación que implementa {@link IAuthService}.
 * <p>
 * Maneja la autenticación de usuarios y la generación de tokens JWT.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class AuthService implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    /**
     * Realiza la autenticación de un usuario y genera un token JWT.
     * <p>
     * Valida las credenciales usando {@link AuthenticationManager}, carga los detalles
     * del usuario mediante {@link CustomUserDetailsService} y genera el token con {@link JwtUtil}.
     * </p>
     *
     * @param loginDto DTO que contiene el nombre de usuario y la contraseña.
     * @return un {@link AuthDto} con el token, nombre de usuario, rol y fecha de expiración.
     */
    @Override
    public AuthDto login(LoginDto loginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        UserDetails user = userDetailsService.loadUserByUsername(loginDto.getUsername());

        String token = jwtUtil.generateToken(user);

        return AuthDto.builder()
                .token(token)
                .username(user.getUsername())
                .role(user.getAuthorities().stream()
                        .findFirst()
                        .map(Object::toString)
                        .orElseThrow(() -> new RuntimeException("Rol no encontrado"))
                ).expiresAt(jwtUtil.getExpiration(token))
                .build();

    }
}
