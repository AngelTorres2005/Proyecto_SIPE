package com.proyectoseminario.proyectoseminario.model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "preguntas")
public class preguntas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pregunta")
    private Integer id_pregunta;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento", nullable = false)
    private instrumentos instrumento;

    @Column(name = "enunciado")
    private String enunciado;

    @Column(name = "orden")
    private Integer orden;

}
