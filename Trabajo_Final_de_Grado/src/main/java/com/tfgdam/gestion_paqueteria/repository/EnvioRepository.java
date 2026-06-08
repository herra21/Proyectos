package com.tfgdam.gestion_paqueteria.repository;

import com.tfgdam.gestion_paqueteria.domain.entity.Camion;
import com.tfgdam.gestion_paqueteria.domain.entity.Envio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface EnvioRepository extends JpaRepository<Envio, Long> {

    List<Envio> findByFechaEnvioBetween (LocalDate fechaIicio, LocalDate fechaFin);

    Optional<Envio> findByCamion(Camion camion);

}
