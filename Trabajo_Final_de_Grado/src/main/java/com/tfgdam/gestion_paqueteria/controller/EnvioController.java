package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.dto.EnvioCreateRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.EnvioResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteCargaDTO;
import com.tfgdam.gestion_paqueteria.service.impl.EnvioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/envios")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @PostMapping("/crearEnvio")
    public ResponseEntity<?> crearEnvio(@RequestBody @Valid EnvioCreateRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            EnvioResponseDTO envioResponseDTO = envioService.crearEnvio(dto);

            response.put("code", 1);
            response.put("message", "Envío creado con éxito.");
            response.put("data", envioResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @GetMapping("/mostrarEnvios")
    public ResponseEntity<?> mostrarEnvios()
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<EnvioResponseDTO> listaResponse = envioService.mostrarTodosLosEnvios();

            response.put("code", 1);
            response.put("message", "Envíos obtenidos con éxito.");
            response.put("data", listaResponse);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (Exception e)
        {
            response.put("code", 0);
            response.put("message", "Error al mostrar los envíos");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/mostrarEnviosPorFecha")
    public ResponseEntity<?> mostrarEnviosPorFecha(@RequestParam LocalDate fechaInicio, @RequestParam LocalDate fechaFin)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<EnvioResponseDTO> listaResponse = envioService.mostrarEnviosPorFechas(fechaInicio, fechaFin);

            response.put("code", 1);
            response.put("message", "Envíos obtenidos con éxito.");
            response.put("data", listaResponse);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @PostMapping("/previsualizarCarga")
    public ResponseEntity<?> previsualizar(@RequestBody EnvioCreateRequestDTO dto)
    {

        Map<String, Object> response = new HashMap<>();
        try
        {
            List<PaqueteCargaDTO> layout = envioService.obtenerPrevisualizacion(
                    dto.getMatriculaVehiculo(),
                    dto.getIdPaquetes()
            );

            response.put("code", 1);
            response.put("message", "Previsualización exitosa");
            response.put("data", layout);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 1);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @PatchMapping("/concluirEnvio")
    public ResponseEntity<?> concluirEnvio(@RequestParam String matricula)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            envioService.finalizarEnvio(matricula);

            response.put("code", 1);
            response.put("message", "Envío concluido. Gracias.");

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }
}
