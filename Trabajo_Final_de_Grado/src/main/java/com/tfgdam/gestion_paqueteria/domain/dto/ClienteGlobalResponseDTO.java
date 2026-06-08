package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClienteGlobalResponseDTO
{
    private long id;
    private String nombre;
    private String telefono;
    private String email;
    private String direccion;
}
