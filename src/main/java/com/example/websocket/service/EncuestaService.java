// src/main/java/com/example/websocket/service/EncuestaService.java
package com.example.websocket.service;

import com.example.websocket.dto.EncuestaCreacionDTO;
import com.example.websocket.dto.EncuestaVotacionDTO;
import com.example.websocket.dto.ResultadoDTO;
import com.example.websocket.model.*;
import com.example.websocket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class EncuestaService {

    private final EncuestaRepository encuestaRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    private VotoRepository votoRepository;

    public EncuestaService(EncuestaRepository encuestaRepository, UsuarioRepository usuarioRepository) {
        this.encuestaRepository = encuestaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public Encuesta crearEncuesta(EncuestaCreacionDTO dto) {
        Encuesta encuesta = new Encuesta();
        encuesta.setTitulo(dto.titulo());
        encuesta.setPreguntas(new ArrayList<>());
        
        // Aquí asumiremos un usuario creador por defecto o lo buscaríamos
        // Usuario creador = usuarioRepository.findById(1L).orElseThrow();
        // encuesta.setCreador(creador);

        for (EncuestaCreacionDTO.PreguntaDTO preguntaDTO : dto.preguntas()) {
            Pregunta pregunta = new Pregunta();
            pregunta.setTexto(preguntaDTO.texto());
            pregunta.setEncuesta(encuesta);
            pregunta.setOpciones(new ArrayList<>());

            for (EncuestaCreacionDTO.OpcionDTO opcionDTO : preguntaDTO.opciones()) {
                Opcion opcion = new Opcion();
                opcion.setTexto(opcionDTO.texto());
                opcion.setPregunta(pregunta);
                pregunta.getOpciones().add(opcion);
            }
            encuesta.getPreguntas().add(pregunta);
        }

        // Obtener el usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        Usuario creador = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        encuesta.setCreador(creador);

        return encuestaRepository.save(encuesta);
    }

    public Encuesta findById(Long id) {
        return encuestaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Encuesta no encontrada con id: " + id));
    }
    
    @Transactional
    public ResultadoDTO calcularResultados(Long encuestaId) {
        Encuesta encuesta = encuestaRepository.findById(encuestaId)
            .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));

        // Mapa: pregunta -> (opción -> votos)
        Map<String, Map<String, Long>> resultados = new LinkedHashMap<>();

        for (Pregunta pregunta : encuesta.getPreguntas()) {
            Map<String, Long> votosPorOpcion = new LinkedHashMap<>();
            for (Opcion opcion : pregunta.getOpciones()) {
                long votos = votoRepository.countByOpcionSeleccionada(opcion);
                votosPorOpcion.put(opcion.getTexto(), votos);
            }
            resultados.put(pregunta.getTexto(), votosPorOpcion);
        }

        return new ResultadoDTO(encuesta.getId(), resultados);
    }

    @Transactional
    public EncuestaVotacionDTO getEncuestaDTO(Long id) {
        Encuesta encuesta = encuestaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Encuesta no encontrada"));
        return EncuestaVotacionDTO.fromEntity(encuesta); // Aquí accedes a preguntas dentro de la sesión
    }
}