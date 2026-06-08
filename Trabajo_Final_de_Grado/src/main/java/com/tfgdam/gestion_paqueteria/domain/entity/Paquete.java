package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import enums.EstadoPaquete;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.*;


@Data
@EqualsAndHashCode(exclude = "envio")
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "paquetes")
public class Paquete {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paquete")
    private long idPaquete;

    @Column
    private String nombre;

    @Column
    private String descripcion;

    @Column(precision = 4, scale = 2)
    private BigDecimal ancho;

    @Column(precision = 4, scale = 2)
    private BigDecimal alto;

    @Column(precision = 4, scale = 2)
    private BigDecimal grosor;

    @Column(precision = 4, scale = 2)
    private BigDecimal peso;

    @Column(precision = 2, scale = 1)
    private BigDecimal fragilidad;

    @Column(precision = 6, scale = 2)
    private BigDecimal posicionX;

    @Column(precision = 6, scale = 2)
    private BigDecimal posicionY;

    @Column(precision = 6, scale = 2)
    private BigDecimal posicionZ;

    @Enumerated(EnumType.STRING)
    private EstadoPaquete estado = EstadoPaquete.PENDIENTE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_envio", nullable = true) // nullable porque el paquete existe antes del envío
    @JsonManagedReference("envio-paquete")
    private Envio envio;

}
