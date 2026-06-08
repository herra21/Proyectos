package com.tfgdam.gestion_paqueteria.service.impl;

import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteCargaDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Envio;
import com.tfgdam.gestion_paqueteria.domain.entity.Paquete;
import com.tfgdam.gestion_paqueteria.repository.CamionRepository;
import com.tfgdam.gestion_paqueteria.repository.EnvioRepository;
import com.tfgdam.gestion_paqueteria.repository.PaqueteRepository;
import enums.EstadoPaquete;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaqueteService {
    @Autowired
    private PaqueteRepository paqueteRepository;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private EnvioRepository envioRepository;


    public PaqueteResponseDTO agregarPaquete(PaqueteRequestDTO dto)
    {
        Paquete paquete = new Paquete();

        paquete.setNombre(dto.getNombre());
        paquete.setDescripcion(dto.getDescripcion());
        paquete.setAncho(BigDecimal.valueOf(dto.getAncho()));
        paquete.setAlto(BigDecimal.valueOf(dto.getAlto()));
        paquete.setGrosor(BigDecimal.valueOf(dto.getGrosor()));
        paquete.setPeso(BigDecimal.valueOf(dto.getPeso()));
        paquete.setFragilidad(BigDecimal.valueOf(dto.getFragilidad()));

        Paquete guardado = paqueteRepository.save(paquete);
        return new PaqueteResponseDTO(guardado.getIdPaquete(), guardado.getNombre(), guardado.getDescripcion(),
            guardado.getAncho(), guardado.getAlto(), guardado.getGrosor(), guardado.getPeso(), guardado.getFragilidad());
    }

    public List<Paquete> consultarPaquete(String nombre)
    {
        return paqueteRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<PaqueteResponseDTO> consultarPaquetesCargados()
    {
        return paqueteRepository.findByEstado(EstadoPaquete.CARGADO)
                .stream()
                .map(p -> new PaqueteResponseDTO(
                        p.getIdPaquete(), p.getNombre(), p.getDescripcion(),
                        p.getAncho(), p.getAlto(), p.getGrosor(), p.getPeso(), p.getFragilidad()))
                .collect(Collectors.toList());
    }

    public List<PaqueteResponseDTO> consultarPaquetesEnviados()
    {
        return paqueteRepository.findByEstado(EstadoPaquete.ENVIADO)
                .stream()
                .map(p -> new PaqueteResponseDTO(
                        p.getIdPaquete(), p.getNombre(), p.getDescripcion(),
                        p.getAncho(), p.getAlto(), p.getGrosor(), p.getPeso(), p.getFragilidad()))
                .collect(Collectors.toList());
    }

    public List<PaqueteResponseDTO> consultarPaquetesPendientes()
    {
        return paqueteRepository.findByEstado(EstadoPaquete.PENDIENTE)
                .stream()
                .map(p -> new PaqueteResponseDTO(
                        p.getIdPaquete(), p.getNombre(), p.getDescripcion(),
                        p.getAncho(), p.getAlto(), p.getGrosor(), p.getPeso(), p.getFragilidad()))
                .collect(Collectors.toList());
    }

    public PaqueteResponseDTO eliminarPaquete(Long id)
    {
        Optional<Paquete> optional = paqueteRepository.findById(id);

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Error. No se encuentra ningún paquete con ID: " + id);
        }

        Paquete eliminar = optional.get();
        paqueteRepository.delete(eliminar);

        return new PaqueteResponseDTO(eliminar.getIdPaquete(), eliminar.getNombre(), eliminar.getDescripcion(),
            eliminar.getAncho(), eliminar.getAlto(), eliminar.getGrosor(), eliminar.getPeso(), eliminar.getFragilidad());
    }

    public void cargarEnvio(String matricula, List<Long> listaPaquetes)
    {

        Camion camion = camionRepository.findById(matricula)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Camión no encontrado"));

        List<Paquete> paquetes = paqueteRepository.findAllById(listaPaquetes);

        if (paquetes.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No hay paquetes para cargar");
        }

        Envio nuevoEnvio = new Envio();
        nuevoEnvio.setCamion(camion);
        nuevoEnvio.setFechaEnvio(LocalDate.now());

        nuevoEnvio = envioRepository.save(nuevoEnvio);

        for (Paquete p : paquetes) {
            p.setEnvio(nuevoEnvio); //
            p.setEstado(EstadoPaquete.CARGADO); //
        }
        paqueteRepository.saveAll(paquetes);
    }

    public PaqueteResponseDTO modificarPaquete(long id, PaqueteModRequestDTO dto)
    {
        Optional<Paquete> optional = paqueteRepository.findById(id);

        if (optional.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Error. No se ha encontrado un paquete con ID: " + id);
        }

        Paquete paquete = optional.get();

        if (dto.getNuevoNombre() != null)    paquete.setNombre(dto.getNuevoNombre());
        if (dto.getNuevaDescripcion() != null) paquete.setDescripcion(dto.getNuevaDescripcion());
        if (dto.getNuevoAncho() != null)  paquete.setAncho(BigDecimal.valueOf(dto.getNuevoAncho()));
        if (dto.getNuevoAlto() != null)     paquete.setAlto(BigDecimal.valueOf(dto.getNuevoAlto()));
        if (dto.getNuevoGrosor() != null) paquete.setGrosor(BigDecimal.valueOf(dto.getNuevoGrosor()));
        if (dto.getNuevoPeso() != null)  paquete.setPeso(BigDecimal.valueOf(dto.getNuevoPeso()));
        if (dto.getNuevaFragilidad() != null)     paquete.setFragilidad(BigDecimal.valueOf(dto.getNuevaFragilidad()));

        Paquete actualizado = paqueteRepository.save(paquete);
        return new PaqueteResponseDTO(actualizado.getIdPaquete(), actualizado.getNombre(), actualizado.getDescripcion(),
            actualizado.getAncho(), actualizado.getAlto(), actualizado.getGrosor(), actualizado.getPeso(), actualizado.getFragilidad());
    }

    public PaqueteResponseDTO cambiarEstado(long id, String nuevoEstado) {
        Optional<Paquete> optional = paqueteRepository.findById(id);
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Paquete no encontrado con ID: " + id);
        }
        Paquete paquete = optional.get();
        try {
            paquete.setEstado(EstadoPaquete.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado no valido: " + nuevoEstado);
        }
        Paquete guardado = paqueteRepository.save(paquete);
        return new PaqueteResponseDTO(
                guardado.getIdPaquete(), guardado.getNombre(), guardado.getDescripcion(),
                guardado.getAncho(), guardado.getAlto(), guardado.getGrosor(),
                guardado.getPeso(), guardado.getFragilidad()
        );
    }

    // Lista todos los paquetes (para el endpoint listarTodos)
    public java.util.List<Paquete> listarTodos() {
        return paqueteRepository.findAll();
    }
}
