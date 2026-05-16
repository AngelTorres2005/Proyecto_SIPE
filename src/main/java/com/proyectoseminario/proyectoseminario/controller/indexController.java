package com.proyectoseminario.proyectoseminario.controller;

import com.proyectoseminario.proyectoseminario.model.*;
import com.proyectoseminario.proyectoseminario.repository.evaluacionesRepository;
import com.proyectoseminario.proyectoseminario.repository.instrumentosRepository;
import com.proyectoseminario.proyectoseminario.repository.pacientesRepository;
import com.proyectoseminario.proyectoseminario.repository.preguntasRepository;
import com.proyectoseminario.proyectoseminario.security.CustomUserDetails;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class indexController {

    @Autowired
    pacientesRepository pacientesRepository;
    @Autowired
    instrumentosRepository instrumentosRepository;
    @Autowired
    preguntasRepository preguntasRepository;

    @Autowired
    evaluacionesRepository evaluacionesRepository;

    private final ChatClient chatClient;

    @Autowired
    public indexController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @GetMapping("/")
    public String loginmedico() {
        return "login-medico";
    }

    @GetMapping("/login-medico")
    public String loginPath(){return "login-medico";}

    @GetMapping("/index")
    public String index() { return "index"; }

    @GetMapping("/panel")
    public String panelMedico() {
        return "panel-medico";
    }

    @GetMapping("/cuestionario")
    public String cuestionario() {
        return "cuestionario-ps";
    }

    @GetMapping("/registro")
    public String registro() { return "registro"; }

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String error, RedirectAttributes redirectAttributes) {
        if (error != null) {
            redirectAttributes.addFlashAttribute("alertaError", "Email o contraseña incorrectos");
            return "redirect:/login-medico";
        }
        return "login-medico";
    }

    @PostMapping("/agregarPaciente")
    public String agregarPaciente(
            @RequestParam String nombre,
            @RequestParam LocalDate fecha_nacimiento,
            @RequestParam String genero,
            @RequestParam String email,
            @RequestParam String observaciones,
            @AuthenticationPrincipal CustomUserDetails customUserDetails,
            RedirectAttributes redirectAttributes
            ){

        usuarios usuarioActual = customUserDetails.getUsuario();

        if(pacientesRepository.findByEmail(email).isPresent()){
            redirectAttributes.addFlashAttribute("alertaError", "El correo ingresado ya se ah registrado anteriormente");
            return "redirect:/panel";
        }

        pacientes nuevoPaciente = new pacientes();
        nuevoPaciente.setNombre(nombre);
        nuevoPaciente.setFecha_nacimiento(fecha_nacimiento);
        nuevoPaciente.setGenero(genero);
        nuevoPaciente.setEmail(email);
        nuevoPaciente.setNotas_clinicas(observaciones);
        nuevoPaciente.setFecha_registro(LocalDate.now());
        nuevoPaciente.setUsuario(usuarioActual);

        pacientesRepository.save(nuevoPaciente);

        redirectAttributes.addFlashAttribute("alertaSuccess", "Paciente agregado correctamente");

        return "redirect:/panel";
    }

    @GetMapping("/pacientesContenedor")
    @ResponseBody
    public List<pacientes> pacientesContenedor(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        return pacientesRepository.findByUsuario(customUserDetails.getUsuario());
    }

    @PostMapping("/eliminarPaciente/{id}")
    @ResponseBody
    public String eliminarPaciente(@PathVariable("id") Integer id){
        try{
            pacientesRepository.deleteById(id);
            return "redirect:/panel";
        }catch (Exception e){
            e.printStackTrace();
            return "redirect:/panel";
        }
    }

    @GetMapping("/editarDatosPaciente")
    public String editarDatosPaciente(RedirectAttributes redirectAttributes, @RequestParam Integer id_paciente, @RequestParam String nombre, @RequestParam String genero, @RequestParam LocalDate fecha_nacimiento, @RequestParam String email, @RequestParam String notas_clinicas){
        try{
            pacientes paciente = pacientesRepository.findById(id_paciente).orElse(null);
            paciente.setNombre(nombre);
            paciente.setGenero(genero);
            paciente.setFecha_nacimiento(fecha_nacimiento);
            paciente.setEmail(email);
            paciente.setNotas_clinicas(notas_clinicas);
            pacientesRepository.save(paciente);
            redirectAttributes.addFlashAttribute("alertaSuccess", "Paciente editado correctamente");
            return "redirect:/panel";
        }catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("alertaError", "Error al editar paciente");
            return "redirect:/panel";
        }
    }

    @GetMapping("/selectInstrumento")
    @ResponseBody
    public List<instrumentos> selectInstrumento(){
        return instrumentosRepository.findAll();
    }

    @GetMapping("/empezarCuestionario")
    public String empezarCuestionario(
            @RequestParam("instrumentoId") Integer instrumentoId,
            @RequestParam("pacienteId") Integer pacienteId,
            Model model){

        model.addAttribute("instrumentoId", instrumentoId);
        model.addAttribute("pacienteId", pacienteId);
        return "cuestionario-ps";
    }

    @GetMapping("/generadorPreguntas/{id}")
    @ResponseBody
    public List<preguntas> generadorPreguntas(@PathVariable("id") Integer id){
        return preguntasRepository.findByInstrumentoId(id);
    }

    @GetMapping("/ia/recomendacion")
    @ResponseBody
    public Map<String, String> obtenerRecomendacion(@RequestParam Integer pacienteId) {
        pacientes paciente = pacientesRepository.findById(pacienteId).orElseThrow();
        evaluaciones ultimaEva = paciente.getEvaluaciones().get(paciente.getEvaluaciones().size() - 1);

        String prompt = String.format(
                "Actúa como un experto en psicología clínica. Basado en la escala PSS de suicido de paykel, el paciente " +
                        "tiene una puntuación de %s (%s). Sus notas clínicas son: %s. " +
                        "Genera 3 recomendaciones breves y profesionales para el médico tratante. Las recomendaciones en forma de lista y lo mas resumidas posibl,es no agregar" +
                        "ninguna informacion adicional. SOLO DAME LA LISTA, NO ALGUNA OTRA PALABRA, La lista en vinetas",
                ultimaEva.getPuntuacion(), ultimaEva.getNivel_riesgo(), paciente.getNotas_clinicas()
        );

        String respuesta = chatClient.prompt().user(prompt).call().content();

        return Map.of("recomendacion", respuesta);
    }

}