package com.tickets.dto;

import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO de respuesta de un ticket.
 * <p>
 * Representa la información completa de un ticket, incluyendo ID, título, descripción,
 * prioridad, estado, asignación, etiquetas y fechas de creación y actualización.
 * </p>
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketResponseDto {
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private String assignee;
    private String tags;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
