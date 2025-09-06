package com.tickets.controller;

import com.tickets.dto.TicketCreateDto;
import com.tickets.dto.TicketResponseDto;
import com.tickets.dto.TicketUpdateDto;
import com.tickets.service.ITicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador de gestión de tickets en la aplicación.
 * <p>
 * Expone los endpoints CRUD para crear, consultar, actualizar y eliminar tickets.
 * Incluye filtros opcionales por estado, prioridad y búsqueda por texto.
 * </p>
 */
@RestController
@RequestMapping("/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final ITicketService iTicketService;

    /**
     * Obtiene un ticket por su ID.
     * <p>
     * Llama al servicio para recuperar los detalles del ticket especificado.
     * </p>
     *
     * @param id el ID del ticket a consultar.
     * @return un {@link ResponseEntity} con el {@link TicketResponseDto} y código 200 OK.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(iTicketService.getById(id));
    }

    /**
     * Obtiene una lista de tickets con filtros opcionales y paginación.
     * <p>
     * Permite filtrar por estado, prioridad y búsqueda por texto, y devuelve
     * un mapa con contenido, total de elementos y total de páginas.
     * </p>
     *
     * @param status filtro por estado del ticket (opcional).
     * @param priority filtro por prioridad del ticket (opcional).
     * @param q búsqueda por texto en título o descripción (opcional).
     * @param page número de página a consultar (por defecto 0).
     * @param limit cantidad de elementos por página (por defecto 10).
     * @return un {@link ResponseEntity} con un mapa que contiene la lista de tickets y la información de paginación.
     */
    @GetMapping
    public ResponseEntity<Map<String, Object>> get(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String priority,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Page<TicketResponseDto> result = iTicketService.get(status, priority, q, page, limit);
        Map<String, Object> response = new HashMap<>();
        response.put("content", result.getContent());
        response.put("totalElements", result.getTotalElements());
        response.put("number", result.getNumber());
        response.put("size", result.getSize());
        return ResponseEntity.ok(response);
    }

    /**
     * Crea un nuevo ticket en el sistema.
     * <p>
     * Recibe un {@link TicketCreateDto} con los datos del ticket y lo guarda mediante el servicio.
     * </p>
     *
     * @param ticketDto los datos del ticket a crear.
     * @return un {@link ResponseEntity} con el {@link TicketResponseDto} creado y código 201 Created.
     */
    @PostMapping
    public ResponseEntity<TicketResponseDto> create(@Valid @RequestBody TicketCreateDto ticketDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(iTicketService.create(ticketDto));
    }

    /**
     * Actualiza un ticket existente.
     * <p>
     * Recibe el ID del ticket y un {@link TicketUpdateDto} con los campos a modificar,
     * y devuelve el ticket actualizado.
     * </p>
     *
     * @param id el ID del ticket a actualizar.
     * @param dto los datos para actualizar el ticket.
     * @return un {@link ResponseEntity} con el {@link TicketResponseDto} actualizado y código 200 OK.
     */
    @PatchMapping("/{id}")
    public ResponseEntity<TicketResponseDto> update(@PathVariable Long id, @Valid @RequestBody TicketUpdateDto dto) {
        return ResponseEntity.ok(iTicketService.update(id, dto));
    }

    /**
     * Elimina un ticket por su ID.
     * <p>
     * Solo usuarios con autoridad 'ADMIN' pueden ejecutar esta operación.
     * </p>
     *
     * @param id el ID del ticket a eliminar.
     * @return un {@link ResponseEntity} con código 204 No Content si la eliminación es exitosa.
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        iTicketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
