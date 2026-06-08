package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.dto.*;
import com.tfgdam.gestion_paqueteria.domain.entity.Usuario;
import com.tfgdam.gestion_paqueteria.service.impl.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController
{
    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrarUsuario")
    public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioRegisterRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            UsuarioRegisterResponseDTO usuarioResponseDTO = usuarioService.registrarUsuario(dto);

            response.put("code", 1);
            response.put("message", "Usuario dado de alta con éxito.");
            response.put("data", usuarioResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUsuario(@RequestBody UsuarioLoginRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            UsuarioLoginResponseDTO usuarioLoginResponseDTO = usuarioService.loginUsuario(dto);

            response.put("code", 1);
            response.put("message", "Usuario logeado con éxito.");
            response.put("data", usuarioLoginResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @GetMapping("/listaUsuarios")
    public ResponseEntity<?> listarUsuarios()
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            List<UsuarioListaResponseDTO> listaUsuarios = usuarioService.listarUsuarios();

            response.put("code", 1);
            response.put("message", "Usuarios obtenidos con éxito.");
            response.put("data", listaUsuarios);

            return ResponseEntity.status(HttpStatus.OK).body(response);
        }catch (Exception e)
        {
            response.put("code", 0);
            response.put("message", "Error al obtener usuarios.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
