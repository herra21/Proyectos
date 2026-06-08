package com.tfgdam.gestion_paqueteria.repository;

import com.tfgdam.gestion_paqueteria.domain.entity.Rol;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RolRepository extends JpaRepository<Rol, Long> {

    Rol findByNombre(String nombre);
}
