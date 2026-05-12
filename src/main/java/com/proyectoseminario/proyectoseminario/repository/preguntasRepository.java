package com.proyectoseminario.proyectoseminario.repository;

import com.proyectoseminario.proyectoseminario.model.preguntas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface preguntasRepository extends JpaRepository<preguntas, Integer> {
    @Query("SELECT p FROM preguntas p WHERE p.instrumento.id_instrumento = :id ORDER BY p.orden ASC")
    List<preguntas> findByInstrumentoId(@Param("id") Integer id);
}