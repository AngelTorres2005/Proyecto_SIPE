package com.proyectoseminario.proyectoseminario.controller;


import com.proyectoseminario.proyectoseminario.model.usuarios;
import com.proyectoseminario.proyectoseminario.repository.usuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class LoginController {

    @Autowired
    private usuariosRepository UsuarioRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;



    @PostMapping("/registro")
    public String registro(
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes redirectAttributes, Model model
    ){
        System.out.println("Método registro llamado con: nombre=" + nombre + ", email=" + email);
        try {
            // validaciones hasta ahora

            if (password.length() < 8) {
                redirectAttributes.addFlashAttribute("alertaError", "La contraseña debe tener mínimo 8 caracteres");
                return "redirect:/registro";
            }

            if (UsuarioRepository.findByEmail(email).isPresent()) {
                redirectAttributes.addFlashAttribute("alertaError", "El correo ya está registrado");
                return "redirect:/registro";
            }

            // encriptar la password
            String passwordEncriptada = passwordEncoder.encode(password);

            // creando el nuevo usuario
            usuarios nuevoUsuario = new usuarios();
            nuevoUsuario.setNombre(nombre);
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setPassword(passwordEncriptada);

            // se guarda el usuario en la base de datos
            usuarios usuarioGuardado = UsuarioRepository.save(nuevoUsuario);

            redirectAttributes.addFlashAttribute("alertaSuccess", "Registro exitoso. Por favor inicia sesión para continuar.");
            return "redirect:/login-medico";

        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("alertaError", "Error al registrar: " + e.getMessage());
            return "redirect:/registro";
        }
    }

}