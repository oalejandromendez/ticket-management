package com.tickets.service;

import com.tickets.dto.TicketCreateDto;
import com.tickets.dto.TicketResponseDto;
import com.tickets.dto.TicketUpdateDto;
import org.springframework.data.domain.Page;

/**
 * Interfaz que define los métodos para la gestión de tickets.
 * <p>
 * Implementada por servicios que manejan operaciones CRUD sobre {@link com.tickets.jpa.entity.TicketEntity}.
 * </p>
 */
public interface ITicketService {
    /**
     * Obtiene un ticket por su identificador.
     *
     * @param id el ID del ticket a buscar.
     * @return un {@link TicketResponseDto} con los datos del ticket.
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra el ticket.
     */
    TicketResponseDto getById(Long id);

    /**
     * Obtiene una lista paginada de tickets filtrados por estado, prioridad y texto de búsqueda.
     *
     * @param status   filtro por estado del ticket (opcional).
     * @param priority filtro por prioridad del ticket (opcional).
     * @param q        texto de búsqueda en título o descripción (opcional).
     * @param page     número de página (0-indexado).
     * @param limit    cantidad de elementos por página.
     * @return una {@link Page} de {@link TicketResponseDto} con los tickets filtrados.
     */
    Page<TicketResponseDto> get(String status, String priority, String q, int page, int limit);

    /**
     * Crea un nuevo ticket en el sistema.
     *
     * @param ticketDto DTO que contiene la información del ticket a crear.
     * @return un {@link TicketResponseDto} con los datos del ticket creado.
     */
    TicketResponseDto create(TicketCreateDto ticketDto);

    /**
     * Actualiza un ticket existente por su ID.
     *
     * @param id  el ID del ticket a actualizar.
     * @param dto DTO con los campos a modificar del ticket.
     * @return un {@link TicketResponseDto} con los datos actualizados del ticket.
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra el ticket.
     */
    TicketResponseDto update(Long id, TicketUpdateDto dto);

    /**
     * Elimina un ticket por su ID.
     *
     * @param id el ID del ticket a eliminar.
     * @throws jakarta.persistence.EntityNotFoundException si no se encuentra el ticket.
     */
    void delete(Long id);
}
