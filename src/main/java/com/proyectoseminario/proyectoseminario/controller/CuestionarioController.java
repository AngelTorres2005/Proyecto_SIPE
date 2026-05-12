package com.proyectoseminario.proyectoseminario.controller;

import com.proyectoseminario.proyectoseminario.model.instrumentos;
import com.proyectoseminario.proyectoseminario.model.pacientes;
import com.proyectoseminario.proyectoseminario.model.evaluaciones;
import com.proyectoseminario.proyectoseminario.model.respuestas_detalle;
import com.proyectoseminario.proyectoseminario.model.preguntas;
import com.proyectoseminario.proyectoseminario.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class CuestionarioController {

    @Autowired
    instrumentosRepository instrumentosRepository;

    @Autowired
    preguntasRepository preguntasRepository;

    @Autowired
    respuestas_detalleRepository respuestasDetalleRepository;

    @Autowired
    evaluacionesRepository evaluacionesRepository;

    @Autowired
    pacientesRepository pacientesRepository;

    @PostMapping("/guardar_evaluacion")
    public String guardarEvaluacion(@RequestParam Map<String, String> formulario, RedirectAttributes redirectAttributes) {

        try {
            Integer pacienteId = Integer.parseInt(formulario.get("pacienteId"));
            Integer instrumentoId = Integer.parseInt(formulario.get("instrumentoId"));

            int puntuacionTotal = 0;
            for (int i = 1; i <= 5; i++) {
                String respuestaDicotomica = formulario.get("p" + i + "_v");
                if (respuestaDicotomica != null) {
                    puntuacionTotal += Integer.parseInt(respuestaDicotomica);
                }
            }

            String nivelRiesgo = "Sin Riesgo";
            if (puntuacionTotal == 1 || puntuacionTotal == 2) {
                nivelRiesgo = "Riesgo Leve";
            } else if (puntuacionTotal == 3) {
                nivelRiesgo = "Riesgo Moderado";
            } else if (puntuacionTotal >= 4) {
                nivelRiesgo = "Riesgo Alto";
            }

            pacientes paciente = pacientesRepository.findById(pacienteId).orElseThrow();
            instrumentos instrumento = instrumentosRepository.findById(instrumentoId).orElseThrow();

            evaluaciones nuevaEvaluacion = new evaluaciones();
            nuevaEvaluacion.setPaciente(paciente);
            nuevaEvaluacion.setInstrumento(instrumento);
            nuevaEvaluacion.setPuntuacion(String.valueOf(puntuacionTotal));
            nuevaEvaluacion.setNivel_riesgo(nivelRiesgo);
            nuevaEvaluacion.setFecha_aplicacion(LocalDate.now());

            evaluaciones evaluacionGuardada = evaluacionesRepository.save(nuevaEvaluacion);

            List<preguntas> listaPreguntas = preguntasRepository.findByInstrumentoId(instrumentoId);

            for (int i = 0; i < 5; i++) {
                String valDicotomico = formulario.get("p" + (i + 1) + "_v");
                String valFrecuencia = formulario.get("p" + (i + 1) + "_f");

                if (valDicotomico != null) {
                    respuestas_detalle detalle = new respuestas_detalle();
                    detalle.setEvaluacion(evaluacionGuardada);
                    detalle.setPregunta(listaPreguntas.get(i));
                    detalle.setRespuesta_dicotomica(valDicotomico.equals("1"));

                    if (valFrecuencia != null && !valFrecuencia.isEmpty()) {
                        detalle.setValor_frecuencia(Integer.parseInt(valFrecuencia));
                    }
                    respuestasDetalleRepository.save(detalle);
                }
            }
            redirectAttributes.addFlashAttribute("alertaSuccess", "Evaluación guardada correctamente. Riesgo: " + nivelRiesgo);
            return "redirect:/panel";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("alertaError", "Error al guardar la evaluación: " + e.getMessage());
            return "redirect:/panel";
        }
    }
}