package com.tickets.jpa.specification;

import com.tickets.jpa.entity.TicketEntity;
import org.springframework.data.jpa.domain.Specification;

/**
 * Clase para definir especificaciones de consulta para la entidad {@link TicketEntity}.
 * <p>
 * Se utiliza para construir consultas dinámicas
 * basadas en criterios como estado, prioridad o búsqueda por texto.
 * </p>
 */
public class TicketSpecification {

    private TicketSpecification() {
    }

    /**
     * Especificación para filtrar tickets por estado.
     *
     * @param status el estado del ticket a filtrar, puede ser nulo.
     * @return una {@link Specification} para usar en consultas dinámicas.
     */
    public static Specification<TicketEntity> hasStatus(String status) {
        return (root, query, cb) -> status == null ? null : cb.equal(root.get("status"), status);
    }

    /**
     * Especificación para filtrar tickets por prioridad.
     *
     * @param priority la prioridad del ticket a filtrar, puede ser nula.
     * @return una {@link Specification} para usar en consultas dinámicas.
     */
    public static Specification<TicketEntity> hasPriority(String priority) {
        return (root, query, cb) -> priority == null ? null : cb.equal(root.get("priority"), priority);
    }

    /**
     * Especificación para filtrar tickets cuyo título o descripción contenga un texto dado.
     *
     * @param q el texto a buscar en título o descripción, puede ser nulo o vacío.
     * @return una {@link Specification} para usar en consultas dinámicas.
     */
    public static Specification<TicketEntity> titleOrDescriptionContains(String q) {
        return (root, query, cb) -> {
            if (q == null || q.isEmpty()) return null;
            String likePattern = "%" + q.toLowerCase() + "%";
            return cb.or(cb.like(cb.lower(root.get("title")), likePattern), cb.like(cb.lower(root.get("description")), likePattern)
            );
        };
    }
}
