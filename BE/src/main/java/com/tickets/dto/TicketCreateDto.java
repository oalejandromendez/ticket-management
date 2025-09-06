package com.tickets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * DTO para la creación de un ticket.
 * <p>
 * Contiene los datos necesarios para crear un nuevo ticket, incluyendo título, descripción,
 * prioridad, estado, asignación y etiquetas. Incluye validaciones de formato y obligatoriedad.
 * </p>
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketCreateDto {

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "La prioridad es obligatoria")
    @Pattern(regexp = "LOW|MEDIUM|HIGH", message = "La prioridad debe ser LOW, MEDIUM o HIGH")
    private String priority;

    @Pattern(regexp = "OPEN|IN_PROGRESS|CLOSED", message = "El estado debe ser OPEN, IN_PROGRESS o CLOSED")
    private String status;

    @NotBlank(message = "La asignación es obligatoria")
    private String assignee;

    @NotBlank(message = "Los tags son obligatorios")
    private String tags;
}
