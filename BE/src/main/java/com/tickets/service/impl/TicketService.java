package com.tickets.service.impl;

import com.tickets.dto.TicketCreateDto;
import com.tickets.dto.TicketResponseDto;
import com.tickets.dto.TicketUpdateDto;
import com.tickets.jpa.entity.TicketEntity;
import com.tickets.jpa.repository.TicketRepository;
import com.tickets.jpa.specification.TicketSpecification;
import com.tickets.mapper.TicketMapper;
import com.tickets.service.ITicketService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * Implementación del servicio {@link ITicketService} para la gestión de tickets.
 * <p>
 * Proporciona operaciones CRUD y búsqueda de tickets con filtros y paginación.
 * </p>
 */
@Service
@RequiredArgsConstructor
public class TicketService implements ITicketService {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private static final String TICKET_NOT_FOUND_MESSAGE = "Ticket no encontrado con id ";

    /**
     * Obtiene un ticket por su ID.
     *
     * @param id el ID del ticket a buscar.
     * @return un {@link TicketResponseDto} con los datos del ticket.
     * @throws EntityNotFoundException si no se encuentra el ticket.
     */
    @Override
    public TicketResponseDto getById(Long id) {
        TicketEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));
        return ticketMapper.toDto(ticket);
    }

    /**
     * Obtiene una lista paginada de tickets aplicando filtros opcionales.
     *
     * @param status   filtro por estado del ticket (opcional).
     * @param priority filtro por prioridad del ticket (opcional).
     * @param q        búsqueda por texto en título o descripción (opcional).
     * @param page     número de página.
     * @param limit    tamaño de la página.
     * @return una {@link Page} de {@link TicketResponseDto} con los tickets filtrados.
     */
    @Override
    public Page<TicketResponseDto> get(String status, String priority, String q, int page, int limit) {
        Specification<TicketEntity> spec = TicketSpecification.hasStatus(status)
                .and(TicketSpecification.hasPriority(priority))
                .and(TicketSpecification.titleOrDescriptionContains(q));

        Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
        Page<TicketEntity> entities = ticketRepository.findAll(spec, pageable);

        return entities.map(ticketMapper::toDto);
    }

    /**
     * Crea un nuevo ticket a partir de los datos proporcionados.
     * Sanitiza el título y la descripción para evitar inyección de HTML.
     * Si no se proporciona estado, se asigna "OPEN" por defecto.
     *
     * @param dto los datos del ticket a crear.
     * @return el {@link TicketResponseDto} del ticket creado.
     */
    @Override
    @Transactional
    public TicketResponseDto create(TicketCreateDto dto) {
        TicketEntity ticket = new TicketEntity();
        ticket.setTitle(Jsoup.clean(dto.getTitle(), Safelist.basic()));
        ticket.setDescription(Jsoup.clean(dto.getDescription(), Safelist.basic()));
        ticket.setPriority(dto.getPriority());
        ticket.setStatus(dto.getStatus() != null ? dto.getStatus() : "OPEN");
        ticket.setAssignee(dto.getAssignee());
        ticket.setTags(dto.getTags());
        TicketEntity savedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDto(savedTicket);
    }

    /**
     * Actualiza un ticket existente identificado por su ID.
     * Solo modifica los campos proporcionados en el DTO.
     * Sanitiza el título y la descripción para evitar inyección de HTML.
     *
     * @param id  el ID del ticket a actualizar.
     * @param dto los datos a actualizar del ticket.
     * @return el {@link TicketResponseDto} del ticket actualizado.
     * @throws EntityNotFoundException si no se encuentra el ticket con el ID dado.
     */
    @Override
    @Transactional
    public TicketResponseDto update(Long id, TicketUpdateDto dto) {
        TicketEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));

        if (dto.getTitle() != null) ticket.setTitle(Jsoup.clean(dto.getTitle(), Safelist.basic()));
        if (dto.getDescription() != null) ticket.setDescription(Jsoup.clean(dto.getDescription(), Safelist.basic()));
        if (dto.getPriority() != null) ticket.setPriority(dto.getPriority());
        if (dto.getStatus() != null) ticket.setStatus(dto.getStatus());
        if (dto.getAssignee() != null) ticket.setAssignee(dto.getAssignee());
        if (dto.getTags() != null) ticket.setTags(dto.getTags());
        TicketEntity updatedTicket = ticketRepository.save(ticket);
        return ticketMapper.toDto(updatedTicket);
    }

    /**
     * Elimina un ticket existente identificado por su ID.
     *
     * @param id el ID del ticket a eliminar.
     * @throws EntityNotFoundException si no se encuentra el ticket con el ID dado.
     */
    @Override
    @Transactional
    public void delete(Long id) {
        TicketEntity ticket = ticketRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(TICKET_NOT_FOUND_MESSAGE + id));
        ticketRepository.delete(ticket);
    }
}
