package com.tickets.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;

/**
 * Entidad que representa un rol de usuario en el sistema.
 * <p>
 * Contiene el ID, el nombre del rol y un indicador de si es un rol administrador.
 * </p>
 */
@Entity
@Table(name = "roles")
@Getter
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private boolean isAdmin;
}
