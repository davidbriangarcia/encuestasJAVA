package com.example.websocket.repository;
import com.example.websocket.model.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {
    List<Voto> findByUsuario_Id(Long idUsuario);
    long countByOpcionSeleccionada(com.example.websocket.model.Opcion opcion);
}
