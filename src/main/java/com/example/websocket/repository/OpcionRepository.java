package com.example.websocket.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import com.example.websocket.model.Opcion;

@Repository
public interface OpcionRepository extends JpaRepository<Opcion, Long> {
    List<Opcion> findByPregunta_Id(Long idPregunta);
}