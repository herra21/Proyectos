package com.tfgdam.gestion_paqueteria.domain.dto;

import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Rol;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioListaResponseDTO
{
    String dni;
    String nombre;
    String apellidos;
    String matricula_camion;
    String rol;
}
