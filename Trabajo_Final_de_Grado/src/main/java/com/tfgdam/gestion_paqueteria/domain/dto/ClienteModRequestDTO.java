package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteModRequestDTO
{
    private String nuevoNombre;
    private String nuevaDireccion;
    private String nuevoTelefono;
    private String nuevoEmail;
}
