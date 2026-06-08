package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import lombok.*;


@Data
@EqualsAndHashCode(exclude = {"paquetes", "cliente", "camion"})
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "envios")
public class Envio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_envio")
    private long idEnvio;

    @Column(name = "fecha_envio")
    private LocalDate fechaEnvio;

    @OneToMany(mappedBy = "envio", cascade = CascadeType.ALL)
    @JsonBackReference("envio-paquete")
    private Set<Paquete> paquetes = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cliente")
    @JsonBackReference("envio-cliente")
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "matricula_camion")
    @JsonBackReference("envio-camion")
    private Camion camion;



}
