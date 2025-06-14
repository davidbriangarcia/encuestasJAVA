// src/main/java/com/example/websocket/controller/EncuestaController.java
package com.example.websocket.controller;

import com.example.websocket.dto.EncuestaCreacionDTO;
import com.example.websocket.dto.EncuestaVotacionDTO;
import com.example.websocket.dto.ResultadoDTO;
import com.example.websocket.model.Encuesta; // Ensure this path matches the actual location of the Encuesta class
import com.example.websocket.service.EncuestaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/encuestas")
public class EncuestaController {

    private final EncuestaService encuestaService;

    public EncuestaController(EncuestaService encuestaService) {
        this.encuestaService = encuestaService;
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> crearEncuesta(@RequestBody EncuestaCreacionDTO encuestaDTO) {
        Encuesta nuevaEncuesta = encuestaService.crearEncuesta(encuestaDTO);
        Map<String, Object> response = new HashMap<>();
        response.put("id", nuevaEncuesta.getId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EncuestaVotacionDTO> getEncuesta(@PathVariable Long id) {
        EncuestaVotacionDTO dto = encuestaService.getEncuestaDTO(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/resultados")
    public ResponseEntity<ResultadoDTO> getResultados(@PathVariable Long id) {
        ResultadoDTO resultado = encuestaService.calcularResultados(id);
        return ResponseEntity.ok(resultado);
    }
}