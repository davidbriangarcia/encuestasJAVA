// src/main/java/com/example/websocket/dto/EncuestaCreacionDTO.java
package com.example.websocket.dto;

import java.util.List;

// Usaremos records de Java para DTOs concisos. Si usas una versión anterior a Java 16, usa una clase normal con getters.
public record EncuestaCreacionDTO(
    String titulo,
    List<PreguntaDTO> preguntas,
    Long creadorId // Opcional: ID del creador de la encuesta
    // Podrías añadir un Long creadorId si manejas usuarios registrados
    
) {
    public record PreguntaDTO(
        String texto,
        List<OpcionDTO> opciones
    ) {}

    public record OpcionDTO(
        String texto
    ) {}
}