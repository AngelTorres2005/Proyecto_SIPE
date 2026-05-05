package com.proyectoseminario.proyectoseminario.repository;

import com.proyectoseminario.proyectoseminario.model.pacientes;
import com.proyectoseminario.proyectoseminario.model.usuarios;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface pacientesRepository extends JpaRepository<pacientes, Integer> {
    Optional<pacientes> findByEmail(String email);
    List<pacientes> findByUsuario(usuarios usuario);
}
