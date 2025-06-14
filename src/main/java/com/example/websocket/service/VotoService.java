package com.example.websocket.service;

import com.example.websocket.dto.ResultadoDTO;
import com.example.websocket.dto.VotoDTO;
import com.example.websocket.model.*;
import com.example.websocket.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final EncuestaRepository encuestaRepository;
    private final PreguntaRepository preguntaRepository;
    private final OpcionRepository opcionRepository;
    private final UsuarioRepository usuarioRepository;

    @Autowired
    public VotoService(
            VotoRepository votoRepository,
            EncuestaRepository encuestaRepository,
            PreguntaRepository preguntaRepository,
            OpcionRepository opcionRepository,
            UsuarioRepository usuarioRepository
    ) {
        this.votoRepository = votoRepository;
        this.encuestaRepository = encuestaRepository;
        this.preguntaRepository = preguntaRepository;
        this.opcionRepository = opcionRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public void registrarVoto(VotoDTO dto, String username) {
        Pregunta pregunta = preguntaRepository.findById(dto.getPreguntaId())
            .orElseThrow(() -> new RuntimeException("Pregunta no encontrada"));
        Opcion opcion = opcionRepository.findById(dto.getOpcionId())
            .orElseThrow(() -> new RuntimeException("Opción no encontrada"));

        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Voto voto = new Voto();
        voto.setPregunta(pregunta);
        voto.setOpcionSeleccionada(opcion);
        voto.setUsuario(usuario);

        votoRepository.save(voto);
    }

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
}