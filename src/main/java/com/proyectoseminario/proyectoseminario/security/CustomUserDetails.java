package com.proyectoseminario.proyectoseminario.security;

import com.proyectoseminario.proyectoseminario.model.usuarios;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private usuarios usuario; // Guardamos tu entidad completa

    public CustomUserDetails(usuarios usuario, Collection<? extends GrantedAuthority> authorities) {
        super(usuario.getEmail(), usuario.getPassword(), authorities);
        this.usuario = usuario;
    }

    public usuarios getUsuario() {
        return usuario;
    }

    public Integer getIdUsuario() {
        return usuario.getId_usuario();
    }
}
