package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamionGlobalResponseDTO
{
    private String matricula;
    private BigDecimal ancho_plataforma;
    private BigDecimal alto_plataforma;
    private BigDecimal largo_plataforma;
    private BigDecimal peso_maximo;
    private String estado;
}
