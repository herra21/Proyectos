package com.tfgdam.gestion_paqueteria.domain.dto;

import com.tfgdam.gestion_paqueteria.domain.entity.Envio;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class EnvioResponseDTO {

    private Long id;
    private LocalDate fechaEnvio;
    private Long idCliente;
    private String matriculaVehiculo;
    private List<PaqueteEnvioResponseDTO> paquetes;

    public static EnvioResponseDTO from(Envio envio) {
        return new EnvioResponseDTO(
            envio.getIdEnvio(),
            envio.getFechaEnvio(),
            envio.getCliente().getIdCliente(),
            envio.getCamion().getMatricula(),
            envio.getPaquetes().stream()
                .map(p -> new PaqueteEnvioResponseDTO(p.getIdPaquete(), p.getNombre()))
                .toList()
        );
    }
}
