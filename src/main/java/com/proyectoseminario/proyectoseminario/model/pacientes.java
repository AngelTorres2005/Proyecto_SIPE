package com.proyectoseminario.proyectoseminario.model;


import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "pacientes")
public class pacientes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_paciente")
    private Integer id_paciente;

    //llave foranea al usuario, un paciente puede tener un solo doctor, pero un doctor puede tener muchos pacientes
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private usuarios usuario;

    @Column(name = "nombre")
    private String nombre;

    @OneToMany(mappedBy = "paciente", fetch = FetchType.EAGER)
    private List<evaluaciones> evaluaciones;

    @Column(name = "email")
    private String email;

    @Column(name = "fecha_nacimiento")
    private java.time.LocalDate fecha_nacimiento;

    @Column(name = "genero")
    private String genero;

    @Column(name = "notas_clinicas")
    private String notas_clinicas;

    @Column(name = "estado_clinico")
    private String estado_clinico;

    @Column(name = "fecha_registro")
    private java.time.LocalDate fecha_registro;
}
