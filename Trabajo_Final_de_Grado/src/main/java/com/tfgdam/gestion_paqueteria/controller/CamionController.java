package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.dto.CamionGlobalResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.CamionModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.CamionNuevoRequestDTO;
import com.tfgdam.gestion_paqueteria.service.impl.CamionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/vehiculos")
public class CamionController {

    @Autowired
    private CamionService camionService;

    // Devuelve todos los camiones registrados
    @GetMapping("/listarVehiculos")
    public ResponseEntity<?> listarVehiculos() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<?> camiones = camionService.listaCamiones();
            response.put("code", 1);
            response.put("message", "Vehículos obtenidos con éxito.");
            response.put("data", camiones);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Da de alta un vehiculo nuevo
    @PostMapping("/agregarVehiculo")
    public ResponseEntity<?> agregarCamion(@RequestBody CamionNuevoRequestDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CamionGlobalResponseDTO camionGlobalResponseDTO = camionService.agregarVehiculo(dto);
            response.put("code", 1);
            response.put("message", "Vehículo agregado con éxito.");
            response.put("data", camionGlobalResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    // Elimina un vehiculo por su matricula
    @DeleteMapping("/eliminarVehiculo/{matricula}")
    public ResponseEntity<?> eliminarVehiculo(@PathVariable String matricula) {
        Map<String, Object> response = new HashMap<>();
        try {
            CamionGlobalResponseDTO camionGlobalResponseDTO = camionService.eliminarVehiculo(matricula);
            response.put("code", 1);
            response.put("message", "Vehículo con matrícula " + matricula + " eliminado con éxito.");
            response.put("data", camionGlobalResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    // Modifica los datos de un vehiculo existente
    @PatchMapping("/actualizarVehiculo/{matricula}")
    public ResponseEntity<?> actualizarVehiculo(@PathVariable String matricula, @RequestBody CamionModRequestDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            CamionGlobalResponseDTO camionGlobalResponseDTO = camionService.modificarVehiculo(matricula, dto);
            response.put("code", 1);
            response.put("message", "Vehículo modificado con éxito.");
            response.put("data", camionGlobalResponseDTO);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (ResponseStatusException e) {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }
    @PatchMapping("/cambiarEstado/{matricula}")
    public ResponseEntity<?> cambiarEstado(@PathVariable String matricula, @RequestParam String estado) {
        Map<String, Object> response = new HashMap<>();
        try {
            CamionGlobalResponseDTO dto = camionService.cambiarEstado(matricula, estado);
            response.put("code", 1);
            response.put("message", "Estado actualizado a " + estado);
            response.put("data", dto);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            response.put("code", 0);
            response.put("message", e.getReason());
            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

}
