package com.tfgdam.gestion_paqueteria.domain.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnvioCreateRequestDTO
{
    @NotNull
    @FutureOrPresent(message = "La fecha no puede ser pasada")
    LocalDate fechaEnvio;
    Long idCliente;
    String matriculaVehiculo;
    List<Long> idPaquetes;
}
