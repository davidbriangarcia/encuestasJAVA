package com.example.websocket.service;

import com.example.websocket.model.Usuario;
import com.example.websocket.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getPassword())
            .roles("USER") // o los roles que manejes
            .build();
    }
}