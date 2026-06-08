package com.tfgdam.gestion_paqueteria.repository;

import com.tfgdam.gestion_paqueteria.domain.dto.PaqueteResponseDTO;
import com.tfgdam.gestion_paqueteria.domain.entity.Paquete;
import enums.EstadoPaquete;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaqueteRepository extends JpaRepository<Paquete, Long>
{
    List<Paquete> findByNombreContainingIgnoreCase(String nombre);

    Optional<Paquete> findById(Long id);

    List<Paquete> findByEstado(EstadoPaquete estadoPaquete);
}
