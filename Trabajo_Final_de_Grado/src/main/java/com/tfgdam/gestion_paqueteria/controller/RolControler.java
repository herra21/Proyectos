package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.entity.Rol;
import com.tfgdam.gestion_paqueteria.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Endpoint para crear los roles ADMIN y EMPLEADO en la BD
// si aún no existen. También permite listarlos.
@RestController
@RequestMapping("/roles")
public class RolControler {

    @Autowired
    private RolRepository rolRepository;

    // Crea los roles por defecto si no existen
    @PostMapping("/inicializar")
    public ResponseEntity<?> inicializar() {
        Map<String, Object> response = new HashMap<>();
        try {
            if (rolRepository.findByNombre("ADMIN") == null) {
                Rol admin = new Rol();
                admin.setNombre("ADMIN");
                rolRepository.save(admin);
            }
            if (rolRepository.findByNombre("EMPLEADO") == null) {
                Rol empleado = new Rol();
                empleado.setNombre("EMPLEADO");
                rolRepository.save(empleado);
            }
            response.put("code", 1);
            response.put("message", "Roles inicializados correctamente.");
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            response.put("code", 0);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Lista todos los roles disponibles
    @GetMapping("/listar")
    public ResponseEntity<?> listar() {
        Map<String, Object> response = new HashMap<>();
        List<Rol> roles = rolRepository.findAll();
        response.put("code", 1);
        response.put("data", roles);
        return ResponseEntity.ok(response);
    }
}
