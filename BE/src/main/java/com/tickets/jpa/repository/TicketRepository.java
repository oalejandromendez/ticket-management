package com.tickets.jpa.repository;

import com.tickets.jpa.entity.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad {@link TicketEntity}.
 * <p>
 * Extiende {@link JpaRepository} para operaciones CRUD y {@link JpaSpecificationExecutor}
 * para consultas con criterios dinámicos.
 * </p>
 */
@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Long>, JpaSpecificationExecutor<TicketEntity> {
}
