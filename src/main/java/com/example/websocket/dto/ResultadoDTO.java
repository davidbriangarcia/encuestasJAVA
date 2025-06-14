package com.example.websocket.dto;

import java.util.Map;

public class ResultadoDTO {
    private Long preguntaId;
    private Map<String, Map<String, Long>> resultados; // opción → votos

    // Constructor to match the required signature
    public ResultadoDTO(Long preguntaId, Map<String, Map<String, Long>> resultados) {
        this.preguntaId = preguntaId;
        this.resultados = resultados;
    }

    public Long getPreguntaId() {
        return preguntaId;
    }

    public void setPreguntaId(Long preguntaId) {
        this.preguntaId = preguntaId;
    }

    public Map<String, Map<String, Long>> getResultados() {
        return resultados;
    }

    public void setResultados(Map<String, Map<String, Long>> resultados) {
        this.resultados = resultados;
    }

    
}