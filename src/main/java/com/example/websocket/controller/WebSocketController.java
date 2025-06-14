// src/main/java/com/example/websocket/controller/WebSocketController.java
package com.example.websocket.controller;

import com.example.websocket.dto.ResultadoDTO;
import com.example.websocket.dto.VotoDTO;
import com.example.websocket.service.VotoService;
import com.example.websocket.service.EncuestaService;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class WebSocketController {

    private final VotoService votoService;
    private final EncuestaService encuestaService;

    public WebSocketController(VotoService votoService, EncuestaService encuestaService) {
        this.votoService = votoService;
        this.encuestaService = encuestaService;
    }

    @MessageMapping("/votar/{encuestaId}")
    @SendTo("/topic/resultados/{encuestaId}")
    public ResultadoDTO procesarVoto(@DestinationVariable Long encuestaId, VotoDTO votoDTO, Principal principal) {
        votoService.registrarVoto(votoDTO, principal.getName());
        return encuestaService.calcularResultados(encuestaId);
    }
}