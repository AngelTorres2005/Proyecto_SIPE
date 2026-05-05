package com.proyectoseminario.proyectoseminario.repository;

import com.proyectoseminario.proyectoseminario.model.usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface usuariosRepository extends JpaRepository<usuarios, Integer> {
    Optional<usuarios> findByEmail(String email);
}
