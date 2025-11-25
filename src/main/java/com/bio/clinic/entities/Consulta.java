package com.bio.clinic.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Table(name = "consultas")
@Entity(name = "Consulta")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "medico_id")
    private Medico medico;

    private Long pacienteId;
    private LocalDateTime dataHora;
    private String tipoConsulta; // Ex: Presencial, Online

    // 1. Construtor Vazio
    public Consulta() {
    }

    // 2. Construtor Completo
    public Consulta(Long id, Medico medico, Long pacienteId, LocalDateTime dataHora, String tipoConsulta) {
        this.id = id;
        this.medico = medico;
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.tipoConsulta = tipoConsulta;
    }

    // --- GETTERS E SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Long getPacienteId() {
        return pacienteId;
    }

    public void setPacienteId(Long pacienteId) {
        this.pacienteId = pacienteId;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getTipoConsulta() {
        return tipoConsulta;
    }

    public void setTipoConsulta(String tipoConsulta) {
        this.tipoConsulta = tipoConsulta;
    }
}