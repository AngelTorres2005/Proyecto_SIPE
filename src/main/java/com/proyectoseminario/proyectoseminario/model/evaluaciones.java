package com.proyectoseminario.proyectoseminario.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "evaluaciones")
public class evaluaciones {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evaluacion")
    private Integer id_evaluacion;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_paciente", nullable = false)
    private pacientes paciente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_instrumento", nullable = false)
    private instrumentos instrumento;

    @Column(name = "puntuacion")
    private String puntuacion;

    @Column(name = "nivel_riesgo")
    private String nivel_riesgo;

    @Column(name = "fecha_aplicacion")
    private java.time.LocalDate fecha_aplicacion;
}
