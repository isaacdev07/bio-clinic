package com.bio.clinic.entities;

import java.time.LocalDateTime;

import com.bio.clinic.enums.StatusConsulta;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
// class consulta finished
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
    private String tipoConsulta;
    
    @Enumerated(EnumType.STRING)
    private StatusConsulta status;

    // 1. Construtor Vazio
    public Consulta() {
    }

    // 2. Construtor Completo
    public Consulta(Long id, Medico medico, Long pacienteId, LocalDateTime dataHora, String tipoConsulta, StatusConsulta status) {
        this.id = id;
        this.medico = medico;
        this.pacienteId = pacienteId;
        this.dataHora = dataHora;
        this.tipoConsulta = tipoConsulta;
        this.status = status;
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
    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        this.status = status;
    }
}
