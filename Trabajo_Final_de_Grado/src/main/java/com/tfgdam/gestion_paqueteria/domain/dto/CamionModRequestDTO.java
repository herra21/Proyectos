package com.tfgdam.gestion_paqueteria.domain.dto;

import enums.EstadoCamion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CamionModRequestDTO
{
    private BigDecimal nuevoAncho_plataforma;
    private BigDecimal nuevoAlto_plataforma;
    private BigDecimal nuevoLargo_plataforma;
    private BigDecimal nuevoPeso_maximo;
}
