package com.tfgdam.gestion_paqueteria.service.impl;
import com.tfgdam.gestion_paqueteria.domain.dto.*;
import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Cliente;
import com.tfgdam.gestion_paqueteria.domain.entity.Envio;
import com.tfgdam.gestion_paqueteria.domain.entity.Paquete;
import com.tfgdam.gestion_paqueteria.repository.CamionRepository;
import com.tfgdam.gestion_paqueteria.repository.ClienteRepository;
import com.tfgdam.gestion_paqueteria.repository.EnvioRepository;
import com.tfgdam.gestion_paqueteria.repository.PaqueteRepository;
import enums.EstadoCamion;
import enums.EstadoPaquete;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private PaqueteRepository paqueteRepository;
    @Autowired
    private CamionRepository camionRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    public EnvioResponseDTO crearEnvio(EnvioCreateRequestDTO dto){

        Optional<Cliente> optionalCliente = clienteRepository.findById(dto.getIdCliente());
        Optional<Camion> optionalCamion = camionRepository.findByMatricula(dto.getMatriculaVehiculo());
        List<Paquete> paquetes = paqueteRepository.findByEstado(EstadoPaquete.PENDIENTE);
        List<Paquete> paquetesEntidades = paqueteRepository.findAllById(dto.getIdPaquetes());

        if (optionalCliente.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente no encontrado con id: " + dto.getIdCliente());
        } else if (optionalCamion.isEmpty())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado con matrícula: " + dto.getMatriculaVehiculo());
        } else if (paquetes.size() != dto.getIdPaquetes().size())
        {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Uno o más paquetes no encontrados");
        }

        Cliente cliente = optionalCliente.get();
        Camion camion = optionalCamion.get();

        // Validaciones de disponibilidad
        if (camion.getEstado() != EstadoCamion.DISPONIBLE) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El camión con matrícula " + dto.getMatriculaVehiculo() + " no está disponible");
        }

        /*
        List<Paquete> paquetesNoDisponibles = paquetes.stream()
            .filter(p -> p.getEstado() != EstadoPaquete.PENDIENTE)
            .toList();

        if (!paquetesNoDisponibles.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Los siguientes paquetes no están disponibles: " +
                paquetesNoDisponibles.stream().map(p -> String.valueOf(p.getIdPaquete())).toList());
        }
        */
        Envio envio = new Envio();
        envio.setFechaEnvio(dto.getFechaEnvio());
        envio.setCamion(camion);
        envio.setCliente(cliente);

        camion.setEstado(EstadoCamion.EN_RUTA);
        camionRepository.save(camion);

        paquetesEntidades.forEach( p -> {
            p.setEstado(EstadoPaquete.CARGADO);
            p.setEnvio(envio);
        });
        paqueteRepository.saveAll(paquetesEntidades);

        envio.getPaquetes().addAll(paquetesEntidades);
        envioRepository.save(envio);

        return EnvioResponseDTO.from(envio);
    }

    public List<EnvioResponseDTO> mostrarTodosLosEnvios()
    {
        return envioRepository.findAll().stream().map(EnvioResponseDTO::from).toList();
    }

    public List<EnvioResponseDTO> mostrarEnviosPorFechas(LocalDate fechaInicio, LocalDate fechaFin) {

        if (fechaInicio.isAfter(fechaFin)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "La fecha inicio no puede ser posterior a la fecha fin.");
        }

        return envioRepository.findByFechaEnvioBetween(fechaInicio, fechaFin)
            .stream()
            .map(EnvioResponseDTO::from)
            .toList();
    }

    public Envio actualizarEnvio(Envio envio){

        return envioRepository.save(envio);
    }

    public void borrarEnvio(Envio envio){

        envioRepository.delete(envio);
    }

    public List<PaqueteCargaDTO> obtenerPrevisualizacion(String matricula, List<Long> idsPaquetes) {
        Camion camion = camionRepository.findByMatricula(matricula)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado"));

        List<Paquete> paquetes = paqueteRepository.findAllById(idsPaquetes);

        return calcularDistribucion(camion, paquetes);
    }

    private List<PaqueteCargaDTO> calcularDistribucion(Camion camion, List<Paquete> paquetes) {
        // Ordenación
        paquetes.sort(Comparator
                .comparingDouble((Paquete p) -> p.getFragilidad().doubleValue())
                .thenComparingDouble((Paquete p) -> p.getPeso().doubleValue()).reversed()
        );

        List<PaqueteCargaDTO> layout = new ArrayList<>();
        double curX = 0, curY = 0, curZ = 0;
        double filaMaxGrosor = 0, pisoMaxAlto = 0;

        // Aquí ya usamos los getters del objeto camion que recibimos por parámetro
        double cAncho = camion.getAnchoPlataforma().doubleValue();
        double cLargo = camion.getLargoPlataforma().doubleValue();
        double cAlto = camion.getAltoPlataforma().doubleValue();

        for (Paquete p : paquetes) {
            double pAncho = p.getAncho().doubleValue();
            double pAlto = p.getAlto().doubleValue();
            double pGrosor = p.getGrosor().doubleValue();

            if (curX + pAncho > cAncho) {
                curX = 0;
                curZ += filaMaxGrosor;
                filaMaxGrosor = 0;
            }
            if (curZ + pGrosor > cLargo) {
                curZ = 0;
                curX = 0;
                curY += pisoMaxAlto;
                pisoMaxAlto = 0;
            }

            if (curY + pAlto <= cAlto) {
                layout.add(new PaqueteCargaDTO(
                        p.getIdPaquete(), curX, curY, curZ,
                        pAncho, pAlto, pGrosor
                ));
                curX += pAncho;
                if (pGrosor > filaMaxGrosor) filaMaxGrosor = pGrosor;
                if (pAlto > pisoMaxAlto) pisoMaxAlto = pAlto;
            }
        }
        return layout;
    }

    public void finalizarEnvio(String matricula)
    {

        Optional<Camion> optionalCamion = camionRepository.findByMatricula(matricula);

        if (optionalCamion.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Vehículo no encontrado con matrícula: " + matricula);
        }

        Camion camion = optionalCamion.get();

        if (camion.getEstado() != EstadoCamion.EN_RUTA) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El camión con matrícula " + matricula + " no está en ruta");
        }

        // Obtener el envío activo del camión
        Envio envio = envioRepository.findByCamion(camion) // ajusta según tu repositorio
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró un envío activo para el camión: " + matricula));

        // Cambiar estado de todos los paquetes a ENTREGADO
        envio.getPaquetes().forEach(p -> p.setEstado(EstadoPaquete.ENTREGADO));
        paqueteRepository.saveAll(envio.getPaquetes());

        // Cambiar estado del camión a DISPONIBLE
        camion.setEstado(EstadoCamion.DISPONIBLE);
        camionRepository.save(camion);
    }
}






