package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaqueteModRequestDTO
{
    private String nuevoNombre;
    private String nuevaDescripcion;
    private Double nuevoAncho;
    private Double nuevoAlto;
    private Double nuevoGrosor;
    private Double nuevoPeso;
    private Double nuevaFragilidad;
}
