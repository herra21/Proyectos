package com.tfgdam.gestion_paqueteria.service.impl;

import com.tfgdam.gestion_paqueteria.domain.dto.ClienteAltaRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.ClienteGlobalResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.ClienteModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Cliente;
import com.tfgdam.gestion_paqueteria.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;


    public List<Cliente> getAllClientes() {

        return clienteRepository.findAll();

    }

    public Optional<Cliente> getById(Long id) {
        return clienteRepository.findById(id);

    }

    public Cliente addCliente(Long id) {
        return null;
    }

    public ClienteGlobalResponseDTO altaCliente(ClienteAltaRequestDTO dto)
    {
        Optional<Cliente> optional = clienteRepository.findByTelefono(dto.getTelefono());

        if (optional.isPresent())
        {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error, ya existe un cliente dado de alta con ese número de telefono.");
        }

        Cliente cliente = new Cliente();
        cliente.setNombre(dto.getNombre());
        cliente.setTelefono(dto.getTelefono());
        cliente.setEmail(dto.getEmail());
        cliente.setDireccion(dto.getDireccion());

        Cliente guardado = clienteRepository.save(cliente);
        return new ClienteGlobalResponseDTO(guardado.getIdCliente(), guardado.getNombre(), guardado.getTelefono(), guardado.getEmail(), guardado.getDireccion());
    }

    public ClienteGlobalResponseDTO eliminarCliente(long id)
    {
        Optional<Cliente> optional = clienteRepository.findById(id);

        if (optional.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No se ha encontrado un cliente con ID: " + id);
        }

        Cliente cliente = optional.get();
        clienteRepository.delete(cliente);
        return new ClienteGlobalResponseDTO(cliente.getIdCliente(), cliente.getNombre(), cliente.getTelefono(), cliente.getEmail(), cliente.getDireccion());
    }

    public ClienteGlobalResponseDTO modificarCliente(long id, ClienteModRequestDTO dto)
    {
        Optional<Cliente> optional = clienteRepository.findById(id);

        if (optional.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. Cliente no encontrado con ID:" + id);
        }

        Cliente cliente = optional.get();

        if (dto.getNuevoNombre() != null)    cliente.setNombre(dto.getNuevoNombre());
        if (dto.getNuevaDireccion() != null) cliente.setDireccion(dto.getNuevaDireccion());
        if (dto.getNuevoTelefono() != null)  cliente.setTelefono(dto.getNuevoTelefono());
        if (dto.getNuevoEmail() != null)     cliente.setEmail(dto.getNuevoEmail());

        Cliente actualizado = clienteRepository.save(cliente);
        return new ClienteGlobalResponseDTO(actualizado.getIdCliente(), actualizado.getNombre(), actualizado.getTelefono(), actualizado.getEmail(), actualizado.getDireccion());
    }
}
