package com.tfgdam.gestion_paqueteria.repository;

import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import enums.EstadoCamion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface CamionRepository extends JpaRepository<Camion, String>
{

    List<Camion> findByEstado(EstadoCamion estadoPaquete);

    Optional<Camion> findByMatricula(String matricula);
}
