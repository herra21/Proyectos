package com.tfgdam.gestion_paqueteria.domain.dto;

import com.tfgdam.gestion_paqueteria.domain.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioLoginResponseDTO
{
    private String dni;
    private String nombreRol;
    private String token;
}
