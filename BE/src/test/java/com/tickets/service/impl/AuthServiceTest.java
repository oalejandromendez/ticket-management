package com.tickets.service.impl;

import com.tickets.dto.AuthDto;
import com.tickets.dto.LoginDto;
import com.tickets.utils.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock private AuthenticationManager authenticationManager;

    @Mock private JwtUtil jwtUtil;

    @Mock private CustomUserDetailsService userDetailsService;

    @InjectMocks private AuthService authService;

    @Test
    void loginValid() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("password");

        UserDetails mockUser = new User(
                "user",
                "password",
                Collections.singletonList(new SimpleGrantedAuthority("USER"))
        );

        when(userDetailsService.loadUserByUsername("user")).thenReturn(mockUser);
        when(jwtUtil.generateToken(mockUser)).thenReturn("jwt");

        Instant expiration = Instant.ofEpochMilli(123456789L);
        when(jwtUtil.getExpiration("jwt")).thenReturn(expiration);
        AuthDto result = authService.login(loginDto);

        assertNotNull(result);
        assertEquals("jwt", result.getToken());
        assertEquals("user", result.getUsername());
        assertEquals("USER", result.getRole());
        assertEquals(expiration, result.getExpiresAt());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("user");
        verify(jwtUtil, times(1)).generateToken(mockUser);
        verify(jwtUtil, times(1)).getExpiration("jwt");
    }

    @Test
    void loginRoleNotFound() {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("user");
        loginDto.setPassword("password");

        UserDetails mockUser = new User("user", "password", Collections.emptyList());

        when(userDetailsService.loadUserByUsername("user")).thenReturn(mockUser);
        when(jwtUtil.generateToken(mockUser)).thenReturn("jwt");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> authService.login(loginDto));

        assertEquals("Rol no encontrado", exception.getMessage());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userDetailsService, times(1)).loadUserByUsername("user");
    }

}