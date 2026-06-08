package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaqueteCargaDTO
{
    private Long paqueteId;
    private double x;
    private double y;
    private double z;
    private double ancho;
    private double alto;
    private double largo;
}
