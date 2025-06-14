package com.example.websocket.dto;

import java.util.List;
import java.util.stream.Collectors;
import com.example.websocket.model.Encuesta;

public class EncuestaVotacionDTO {
    public Long id;
    public String titulo;
    public List<PreguntaDTO> preguntas;

    public static class PreguntaDTO {
        public Long id;
        public String texto;
        public List<OpcionDTO> opciones;
    }

    public static class OpcionDTO {
        public Long id;
        public String texto;
    }

    public static EncuestaVotacionDTO fromEntity(Encuesta encuesta) {
        EncuestaVotacionDTO dto = new EncuestaVotacionDTO();
        dto.id = encuesta.getId();
        dto.titulo = encuesta.getTitulo();
        dto.preguntas = encuesta.getPreguntas().stream().map(p -> {
            PreguntaDTO pdto = new PreguntaDTO();
            pdto.id = p.getId();
            pdto.texto = p.getTexto();
            pdto.opciones = p.getOpciones().stream().map(o -> {
                OpcionDTO odto = new OpcionDTO();
                odto.id = o.getId();
                odto.texto = o.getTexto();
                return odto;
            }).collect(Collectors.toList());
            return pdto;
        }).collect(Collectors.toList());
        return dto;
    }
}