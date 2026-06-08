package com.tfgdam.gestion_paqueteria.service.impl;

import com.tfgdam.gestion_paqueteria.domain.dto.CamionGlobalResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.CamionModRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.CamionNuevoRequestDTO;
import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteCargaDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Paquete;
import com.tfgdam.gestion_paqueteria.repository.CamionRepository;
import com.tfgdam.gestion_paqueteria.repository.PaqueteRepository;
import enums.EstadoCamion;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CamionService {

    @Autowired
    private CamionRepository camionRepository;

    @Autowired
    private PaqueteRepository paqueteRepository;

    // Devuelve todos los camiones registrados
    public List<Camion> listaCamiones() {
        return camionRepository.findAll();
    }

    // Busca un camion por matricula para asignarlo
    public Optional<Camion> asignarCamion(String matricula) {
        return camionRepository.findById(matricula);
    }

    // Lista los camiones que estan disponibles
    public List<Camion> camionesDisponibles() {
        return camionRepository.findByEstado(EstadoCamion.DISPONIBLE);
    }

    // Lista los camiones que estan en ruta
    public List<Camion> camionesEnRuta() {
        return camionRepository.findByEstado(EstadoCamion.EN_RUTA);
    }

    // Registra un nuevo vehiculo en el sistema
    public CamionGlobalResponseDTO agregarVehiculo(CamionNuevoRequestDTO dto)
    {
        Optional<Camion> optional = camionRepository.findByMatricula(dto.getMatricula());

        // Si ya existe un camion con esa matricula, no se puede crear otro
        if (optional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Error. Ya existe un vehículo dado de alta con matrícula: " + dto.getMatricula());
        }

        // Creamos el camion nuevo con los datos del DTO
        Camion camion = new Camion();
        camion.setMatricula(dto.getMatricula());
        camion.setAnchoPlataforma(dto.getAncho_plataforma());
        camion.setAltoPlataforma(dto.getAlto_plataforma());
        camion.setLargoPlataforma(dto.getLargo_plataforma());
        camion.setPesoMaximo(dto.getPeso_maximo());
        camion.setEstado(EstadoCamion.DISPONIBLE);

        camionRepository.save(camion);
        return new CamionGlobalResponseDTO(
            camion.getMatricula(),
            camion.getAnchoPlataforma(),
            camion.getAltoPlataforma(),
            camion.getLargoPlataforma(),
            camion.getPesoMaximo(),
            camion.getEstado().toString()
        );
    }

    // Elimina un vehiculo por matricula
    public CamionGlobalResponseDTO eliminarVehiculo(String matricula)
    {
        Optional<Camion> optional = camionRepository.findByMatricula(matricula.trim());

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Error. Vehículo no encontrado con matrícula: " + matricula);
        }

        Camion camion = optional.get();

        // No se puede eliminar si esta en ruta
        if (camion.getEstado().equals(EstadoCamion.EN_RUTA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Error. El vehículo con matrícula " + matricula + " está en ruta y no se puede eliminar.");
        }

        camionRepository.delete(camion);
        return new CamionGlobalResponseDTO(
            camion.getMatricula(),
            camion.getAnchoPlataforma(),
            camion.getAltoPlataforma(),
            camion.getLargoPlataforma(),
            camion.getPesoMaximo(),
            "Eliminado"
        );
    }

    // Actualiza los datos de un vehiculo existente
    public CamionGlobalResponseDTO modificarVehiculo(String matricula, CamionModRequestDTO dto)
    {
        Optional<Camion> optional = camionRepository.findByMatricula(matricula.trim());

        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                "Error. Vehículo no encontrado con matrícula: " + matricula);
        }

        Camion camion = optional.get();

        // Tampoco se puede modificar si esta en ruta
        if (camion.getEstado().equals(EstadoCamion.EN_RUTA)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                "Error. El vehículo con matrícula " + matricula + " está en ruta y no se puede modificar.");
        }

        // Solo actualizamos los campos que vienen rellenos en el DTO
        if (dto.getNuevoAncho_plataforma() != null) camion.setAnchoPlataforma(dto.getNuevoAncho_plataforma());
        if (dto.getNuevoAlto_plataforma()  != null) camion.setAltoPlataforma(dto.getNuevoAlto_plataforma());
        if (dto.getNuevoLargo_plataforma() != null) camion.setLargoPlataforma(dto.getNuevoLargo_plataforma());
        if (dto.getNuevoPeso_maximo()      != null) camion.setPesoMaximo(dto.getNuevoPeso_maximo());

        Camion actualizado = camionRepository.save(camion);
        return new CamionGlobalResponseDTO(
            actualizado.getMatricula(),
            actualizado.getAnchoPlataforma(),
            actualizado.getAltoPlataforma(),
            actualizado.getLargoPlataforma(),
            actualizado.getPesoMaximo(),
            actualizado.getEstado().toString()
        );
    }

    public CamionGlobalResponseDTO cambiarEstado(String matricula, String nuevoEstado) {
        Optional<Camion> optional = camionRepository.findByMatricula(matricula.trim());
        if (optional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Vehículo no encontrado: " + matricula);
        }
        Camion camion = optional.get();
        try {
            camion.setEstado(EstadoCamion.valueOf(nuevoEstado.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Estado no válido: " + nuevoEstado);
        }
        return toDTO(camionRepository.save(camion));
    }

    private CamionGlobalResponseDTO toDTO(Camion c) {
        return new CamionGlobalResponseDTO(c.getMatricula(), c.getAnchoPlataforma(),
                c.getAltoPlataforma(), c.getLargoPlataforma(), c.getPesoMaximo(), c.getEstado().toString());
    }

}
