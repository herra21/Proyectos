package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaqueteRequestDTO
{
    private String nombre;
    private String descripcion;
    private double ancho;
    private double alto;
    private double grosor;
    private double peso;
    private double fragilidad;
}
