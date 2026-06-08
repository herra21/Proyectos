package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "clienteEnvios")
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private long idCliente;

    @Column
    private String nombre;

    @Column
    private String telefono;

    @Column
    private String email;

    @Column
    private String direccion;

    @OneToMany(mappedBy = "cliente")
    @JsonManagedReference("envio-cliente")
    private Set<Envio> clienteEnvios = new HashSet<>();

}
