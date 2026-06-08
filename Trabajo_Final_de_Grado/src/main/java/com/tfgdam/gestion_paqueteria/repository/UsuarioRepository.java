package com.tfgdam.gestion_paqueteria.repository;

import com.tfgdam.gestion_paqueteria.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, String>
{
    Optional<Usuario> findById (String dni);

}
