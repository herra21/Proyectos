package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Paquete;
import com.tfgdam.gestion_paqueteria.service.impl.PaqueteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/paquetes")
public class PaqueteController
{

    @Autowired
    private PaqueteService paqueteService;

    // Crear paquete nuevo
    @PostMapping("/agregarPaquete")
    public ResponseEntity<?> agregarPaquete(@RequestBody PaqueteRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();
        try
        {
            PaqueteResponseDTO paqueteResponseDTO = paqueteService.agregarPaquete(dto);
            response.put("code", 1);
            response.put("message", "Paquete dado de alta con éxito.");
            response.put("data", paqueteResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (Exception e)
        {
            response.put("code", 0);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    // Buscar paquetes por nombre (búsqueda parcial, sin distinguir mayúsculas)
    // Devuelve siempre code:1, aunque la lista esté vacía, para que el frontend lo maneje bien
    @GetMapping("/consultarPaquete")
    public ResponseEntity<?> consultarPaquete(@RequestParam String nombre)
    {
        Map<String, Object> response = new HashMap<>();
        List<Paquete> paquetes = paqueteService.consultarPaquete(nombre);

        // Convertimos a DTO para evitar problemas de serialización con las relaciones JPA
        List<PaqueteResponseDTO> dtos = paquetes.stream()
            .map(p -> new PaqueteResponseDTO(
                p.getIdPaquete(),
                p.getNombre(),
                p.getDescripcion(),
                p.getAncho(),
                p.getAlto(),
                p.getGrosor(),
                p.getPeso(),
                p.getFragilidad()
            ))
            .collect(Collectors.toList());

        response.put("code", 1);
        response.put("message", paquetes.isEmpty()
            ? "No se encontraron paquetes con el nombre: " + nombre
            : "Paquetes encontrados con éxito.");
        response.put("size", dtos.size());
        response.put("data", dtos);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    // Listar todos los paquetes (para la tabla del panel admin)
    @GetMapping("/listarTodos")
    public ResponseEntity<?> listarTodos()
    {
        Map<String, Object> response = new HashMap<>();
        List<Paquete> paquetes = paqueteService.consultarPaquete("");
        List<PaqueteResponseDTO> dtos = paquetes.stream()
            .map(p -> new PaqueteResponseDTO(
                p.getIdPaquete(),
                p.getNombre(),
                p.getDescripcion(),
                p.getAncho(),
                p.getAlto(),
                p.getGrosor(),
                p.getPeso(),
                p.getFragilidad()
            ))
            .collect(Collectors.toList());
        response.put("code", 1);
        response.put("message", "Paquetes obtenidos con éxito.");
        response.put("data", dtos);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/listarPendientes")
    public ResponseEntity<?> listarPendientes()
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<PaqueteResponseDTO> listaPaquetes =  paqueteService.consultarPaquetesPendientes();

            response.put("code", 1);
            response.put("message", "Paquetes obtenidos con éxito");
            response.put("data", listaPaquetes);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e)
        {
            response.put("code", 0);
            response.put("message", "Error al obtener paquetes.");

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    // Eliminar paquete por ID
    @DeleteMapping("/eliminarPaquete/{id}")
    public ResponseEntity<?> eliminarPaquete(@PathVariable Long id)
    {
        Map<String, Object> response = new HashMap<>();
        try
        {
            PaqueteResponseDTO paqueteResponseDTO = paqueteService.eliminarPaquete(id);
            response.put("code", 1);
            response.put("message", "Paquete eliminado con éxito.");
            response.put("data", paqueteResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    // Modificar paquete — solo los campos enviados cambian
    @PatchMapping("/modificarPaquete/{id}")
    public ResponseEntity<?> modificarPaquete(@PathVariable long id, @RequestBody PaqueteModRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();
        try
        {
            PaqueteResponseDTO paqueteResponseDTO = paqueteService.modificarPaquete(id, dto);
            response.put("code", 1);
            response.put("message", "Datos del paquete modificados con éxito.");
            response.put("data", paqueteResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @GetMapping("/obtenerPaquetesEnviados")
    public ResponseEntity<?> paquetesEnviados()
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<PaqueteResponseDTO> listaPaquetes = paqueteService.consultarPaquetesEnviados();

            response.put("code", 1);
            response.put("message", "Paquetes obtenidos con éxito.");
            response.put("data", listaPaquetes);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @GetMapping("/obtenerPaquetesCargados")
    public ResponseEntity<?> paquetesCargados()
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<PaqueteResponseDTO> listaPaquetes = paqueteService.consultarPaquetesCargados();

            response.put("code", 1);
            response.put("message", "Paquetes obtenidos con éxito.");
            response.put("data", listaPaquetes);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    // Cambia el estado de un paquete (PENDIENTE, CARGADO, EN_RUTA, ENTREGADO, INCIDENCIA...)
    @PatchMapping("/cambiarEstado/{id}")
    public ResponseEntity<?> cambiarEstado(@PathVariable long id, @RequestParam String estado)
    {
        Map<String, Object> response = new HashMap<>();
        try
        {
            PaqueteResponseDTO result = paqueteService.cambiarEstado(id, estado);
            response.put("code", 1);
            response.put("message", "Estado actualizado a " + estado);
            response.put("data", result);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

}
