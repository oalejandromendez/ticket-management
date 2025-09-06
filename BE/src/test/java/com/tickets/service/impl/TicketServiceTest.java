package com.tickets.service.impl;

import com.tickets.dto.TicketCreateDto;
import com.tickets.dto.TicketResponseDto;
import com.tickets.dto.TicketUpdateDto;
import com.tickets.jpa.entity.TicketEntity;
import com.tickets.jpa.repository.TicketRepository;
import com.tickets.mapper.TicketMapper;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock private TicketRepository ticketRepository;
    @Mock private TicketMapper ticketMapper;

    @InjectMocks private TicketService ticketService;

    @Test
    void testGetByIdSuccess() {
        TicketEntity entity = new TicketEntity();
        entity.setId(1L);
        entity.setTitle("Test Ticket");

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(1L);
        dto.setTitle("Test Ticket");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ticketMapper.toDto(entity)).thenReturn(dto);

        TicketResponseDto result = ticketService.getById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Test Ticket");
    }

    @Test
    void testGetByIdNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.getById(1L));
    }

    @Test
    void testGetWithFilters() {
        TicketEntity entity = new TicketEntity();
        entity.setId(1L);
        entity.setTitle("Filtered Ticket");

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(1L);
        dto.setTitle("Filtered Ticket");

        Page<TicketEntity> page = new PageImpl<>(List.of(entity));
        when(ticketRepository.findAll((Specification<TicketEntity>) any(), any(Pageable.class)))
                .thenReturn(page);

        when(ticketMapper.toDto(entity)).thenReturn(dto);

        Page<TicketResponseDto> result = ticketService.get("OPEN", "HIGH", "Test", 0, 10);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitle()).isEqualTo("Filtered Ticket");
    }

    @Test
    void testCreate() {
        TicketCreateDto createDto = new TicketCreateDto();
        createDto.setTitle("New Ticket");
        createDto.setDescription("Desc");
        createDto.setPriority("HIGH");
        createDto.setStatus(null);

        TicketEntity entity = new TicketEntity();
        entity.setId(1L);
        entity.setTitle("New Ticket");
        entity.setStatus("OPEN");

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(1L);
        dto.setTitle("New Ticket");

        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(entity);
        when(ticketMapper.toDto(entity)).thenReturn(dto);

        TicketResponseDto result = ticketService.create(createDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New Ticket");
    }

    @Test
    void testUpdateSuccess() {
        TicketUpdateDto updateDto = new TicketUpdateDto();
        updateDto.setTitle("Updated Title");

        TicketEntity entity = new TicketEntity();
        entity.setId(1L);
        entity.setTitle("Old Title");

        TicketEntity savedEntity = new TicketEntity();
        savedEntity.setId(1L);
        savedEntity.setTitle("Updated Title");

        TicketResponseDto dto = new TicketResponseDto();
        dto.setId(1L);
        dto.setTitle("Updated Title");

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(ticketRepository.save(entity)).thenReturn(savedEntity);
        when(ticketMapper.toDto(savedEntity)).thenReturn(dto);

        TicketResponseDto result = ticketService.update(1L, updateDto);

        assertThat(result.getTitle()).isEqualTo("Updated Title");
    }

    @Test
    void testUpdateNotFound() {
        TicketUpdateDto updateDto = new TicketUpdateDto();
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> ticketService.update(1L, updateDto));
    }

    @Test
    void testDeleteSuccess() {
        TicketEntity entity = new TicketEntity();
        entity.setId(1L);

        when(ticketRepository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(ticketRepository).delete(entity);

        ticketService.delete(1L);

        verify(ticketRepository, times(1)).delete(entity);
    }

    @Test
    void testDeleteNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> ticketService.delete(1L));
    }

}