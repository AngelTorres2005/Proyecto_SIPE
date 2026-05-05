package com.proyectoseminario.proyectoseminario.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "instrumentos")
public class instrumentos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_instrumento")
    private Integer id_instrumento;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "descripcion")
    private String descripcion;

}
