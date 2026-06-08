package com.tfgdam.gestion_paqueteria.domain.dto;

import lombok.Data;

@Data
public class UsuarioRegisterRequestDTO
{
    private String dni;
    private String nombre;
    private String apellidos;
    private String contrasenia;
    private String rol;
}
