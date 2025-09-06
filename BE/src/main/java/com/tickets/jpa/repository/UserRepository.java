package com.tickets.jpa.repository;

import com.tickets.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositorio para la entidad {@link UserEntity}.
 * <p>
 * Proporciona operaciones CRUD y un método para buscar un usuario por su nombre de usuario.
 * </p>
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}
