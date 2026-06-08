package com.tfgdam.gestion_paqueteria.controller;

import com.tfgdam.gestion_paqueteria.domain.dto.ClienteAltaRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.ClienteGlobalResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.ClienteModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Cliente;
import com.tfgdam.gestion_paqueteria.service.impl.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    ClienteService clienteService;

    @PostMapping("/altaCliente")
    public ResponseEntity<?> altaCliente(@RequestBody ClienteAltaRequestDTO dto)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            ClienteGlobalResponseDTO clienteGlobalResponseDTO = clienteService.altaCliente(dto);

            response.put("code", 1);
            response.put("message", "Cliente dado de alta con éxito");
            response.put("data", clienteGlobalResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @DeleteMapping("/bajaCliente")
    public ResponseEntity<?> bajaCliente(@RequestParam long id)
    {
        Map<String, Object> response = new HashMap<>();

        try
        {
            ClienteGlobalResponseDTO clienteGlobalResponseDTO = clienteService.eliminarCliente(id);

            response.put("code", 1);
            response.put("message", "Cliente dado de baja con éxito.");
            response.put("data", clienteGlobalResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 0);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @PatchMapping("/modificarDatosCliente/{id}")
    public ResponseEntity<?> modificarCliente(@PathVariable long id, @RequestBody ClienteModRequestDTO dto)
    {
        Map <String, Object> response = new HashMap<>();

        try
        {
            ClienteGlobalResponseDTO clienteGlobalResponseDTO = clienteService.modificarCliente(id, dto);

            response.put("code", 1);
            response.put("message", "Datos del cliente modificados con éxito.");
            response.put("data", clienteGlobalResponseDTO);

            return ResponseEntity.status(HttpStatus.OK).body(response);

        }catch (ResponseStatusException e)
        {
            response.put("code", 1);
            response.put("message", e.getReason());

            return ResponseEntity.status(e.getStatusCode()).body(response);
        }
    }

    @GetMapping("/listar")
    public ResponseEntity<?> listar()
    {
        Map <String, Object> response = new HashMap<>();

        List<Cliente> listaClientes = clienteService.getAllClientes();

        response.put("code", 1);
        response.put("message", "Clientes obtenidos con éxito.");
        response.put("data", listaClientes);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


}
