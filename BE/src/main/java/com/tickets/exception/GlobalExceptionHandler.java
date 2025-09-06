package com.tickets.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

/**
 * Manejador global de excepciones para la aplicación.
 * <p>
 * Esta clase captura y maneja de forma centralizada las excepciones lanzadas
 * por los controladores REST, permitiendo enviar respuestas consistentes
 * al cliente con códigos HTTP apropiados y mensajes claros.
 * </p>
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Maneja las excepciones de tipo {@link EntityNotFoundException}.
     * <p>
     * Devuelve un {@link ResponseEntity} con código 404 Not Found y un cuerpo que contiene
     * el mensaje de error.
     * </p>
     *
     * @param ex la excepción lanzada cuando no se encuentra la entidad.
     * @return un {@link ResponseEntity} con el mensaje de error y código 404.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFound(EntityNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    /**
     * Maneja las excepciones de tipo {@link AccessDeniedException}.
     * <p>
     * Devuelve un {@link ResponseEntity} con código 403 Forbidden y un mensaje indicando
     * que el usuario no tiene permisos para realizar la acción.
     * </p>
     *
     * @param ex la excepción lanzada cuando el acceso es denegado.
     * @return un {@link ResponseEntity} con el mensaje de error y código 403.
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDenied(AccessDeniedException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "No tienes permisos para realizar esta acción");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }
}
