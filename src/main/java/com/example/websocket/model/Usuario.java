package com.example.websocket.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "creador")
    private Set<Encuesta> encuestasCreadas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<Encuesta> getEncuestasCreadas() {
        return encuestasCreadas;
    }

    public void setEncuestasCreadas(Set<Encuesta> encuestasCreadas) {
        this.encuestasCreadas = encuestasCreadas;
    }

}