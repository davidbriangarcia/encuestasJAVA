// src/main/java/com/example/websocket/dto/VotoDTO.java
package com.example.websocket.dto;

public class VotoDTO {
    private Long preguntaId;
    private Long opcionId;

    public VotoDTO() {}
    

    public Long getPreguntaId() { return preguntaId; }
    public void setPreguntaId(Long preguntaId) { this.preguntaId = preguntaId; }
    public Long getOpcionId() { return opcionId; }
    public void setOpcionId(Long opcionId) { this.opcionId = opcionId; }
}