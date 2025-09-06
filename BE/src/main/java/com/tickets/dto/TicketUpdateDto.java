package com.tickets.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO para la actualización de un ticket.
 * <p>
 * Contiene los campos opcionales que se pueden modificar de un ticket existente.
 * </p>
 */
@Getter
@Setter
public class TicketUpdateDto {
    private String title;
    private String description;
    private String priority;
    private String status;
    private String assignee;
    private String tags;
}