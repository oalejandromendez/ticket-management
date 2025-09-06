package com.tickets.mapper;

import com.tickets.dto.TicketResponseDto;
import com.tickets.jpa.entity.TicketEntity;
import org.mapstruct.Mapper;

/**
 * Mapper para la entidad {@link TicketEntity}.
 * <p>
 * Define las conversiones entre entidades y DTOs relacionados con tickets.
 * Se utiliza con MapStruct para generar automáticamente las implementaciones.
 * </p>
 */
@Mapper(componentModel = "spring")
public interface TicketMapper {

    /**
     * Convierte una {@link TicketEntity} a {@link TicketResponseDto}.
     *
     * @param entity la entidad de ticket a convertir, puede ser nula.
     * @return el DTO correspondiente o null si la entidad es nula.
     */
    default TicketResponseDto toDto(TicketEntity entity) {
        if (entity == null) return null;
        return TicketResponseDto.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .priority(entity.getPriority())
                .status(entity.getStatus())
                .assignee(entity.getAssignee())
                .tags(entity.getTags())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
