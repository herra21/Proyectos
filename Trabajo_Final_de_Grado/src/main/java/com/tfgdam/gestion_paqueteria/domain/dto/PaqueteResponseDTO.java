package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaqueteResponseDTO
{
    private long id;
    private String nombre;
    private String descripcion;
    private BigDecimal ancho;
    private BigDecimal alto;
    private BigDecimal grosor;
    private BigDecimal peso;
    private BigDecimal fragilidad;
}
