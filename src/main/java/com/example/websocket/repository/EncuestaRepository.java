package com.example.websocket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.websocket.model.Encuesta;
import java.util.List;

@Repository
public interface EncuestaRepository extends JpaRepository<Encuesta, Long> {
    List<Encuesta> findByCreador_Id(Long idCreador);
}

