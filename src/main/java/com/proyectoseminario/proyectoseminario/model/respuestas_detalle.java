package com.proyectoseminario.proyectoseminario.model;


import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@Entity
@Data
@Table(name = "respuestas_detalle")
public class respuestas_detalle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id_detalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_evaluacion", nullable = false)
    private evaluaciones evaluacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pregunta", nullable = false)
    private preguntas pregunta;

    @Column(name="respuesta_dicotomica")
    private Boolean respuesta_dicotomica;

    @Column(name = "valor_frecuencia")
    private Integer valor_frecuencia;



}
