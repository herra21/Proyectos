package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.*;


@Data
@EqualsAndHashCode(exclude = "rolUsuarios")
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "roles")
public class Rol {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rol")
    private long idRol;

    @Column
    private String nombre;

    @OneToMany(mappedBy = "rol")
    @JsonManagedReference("usuario-rol")
    private Set<Usuario> rolUsuarios = new HashSet<>();

}
