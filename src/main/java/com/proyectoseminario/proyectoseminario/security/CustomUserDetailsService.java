package com.proyectoseminario.proyectoseminario.security;

import com.proyectoseminario.proyectoseminario.model.usuarios;
import com.proyectoseminario.proyectoseminario.model.usuarios;
import com.proyectoseminario.proyectoseminario.repository.usuariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private usuariosRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        usuarios usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        //Traducimos objeto 'usuarios' a un objeto 'User' de Spring Security
        return new CustomUserDetails(
                usuario,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}
