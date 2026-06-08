package com.tfgdam.gestion_paqueteria.domain.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import enums.EstadoCamion;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.*;

@Data
@EqualsAndHashCode(exclude = "envios")
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "camiones")
public class Camion {

    @Id
    @Column(name = "matricula")
    private String matricula;

    @Column(name = "ancho_plataforma", precision = 6, scale = 2)
    private BigDecimal anchoPlataforma;

    @Column(name = "alto_plataforma", precision = 6, scale = 2)
    private BigDecimal altoPlataforma;

    @Column(name = "largo_plataforma", precision = 6, scale = 2)
    private BigDecimal largoPlataforma;

    @Column(name = "peso_maximo", precision = 10, scale = 2)
    private BigDecimal pesoMaximo;

    @Enumerated(EnumType.STRING)
    private EstadoCamion estado = EstadoCamion.DISPONIBLE;

    @OneToMany(mappedBy = "camion")
    @JsonManagedReference("envio-camion")
    private Set<Envio> envios = new HashSet<>();

}
