package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@EqualsAndHashCode(exclude = {"camion", "rol"})
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "usuarios")
public class Usuario {

    @Id
    private String dni;

    @Column
    private String nombre;

    @Column
    private String apellidos;

    @Column
    private String contrasenia;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_camion")
    private Camion camion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    @JsonBackReference("usuario-rol")
    private Rol rol;

}
