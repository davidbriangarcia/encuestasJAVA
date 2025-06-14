package com.example.websocket.model;

import jakarta.persistence.*;

@Entity
@Table(name = "votos", uniqueConstraints = {
    // Un usuario solo puede votar una vez por pregunta
    @UniqueConstraint(columnNames = {"usuario_id", "pregunta_id"})
})
public class Voto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // El usuario que emitió el voto
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // La pregunta que se respondió
    @ManyToOne
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta pregunta;
    
    // La opción que se seleccionó
    @ManyToOne
    @JoinColumn(name = "opcion_id", nullable = false)
    private Opcion opcionSeleccionada;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Pregunta getPregunta() {
        return pregunta;
    }

    public void setPregunta(Pregunta pregunta) {
        this.pregunta = pregunta;
    }

    public Opcion getOpcionSeleccionada() {
        return opcionSeleccionada;
    }

    public void setOpcionSeleccionada(Opcion opcionSeleccionada) {
        this.opcionSeleccionada = opcionSeleccionada;
    }
    }