package com.bio.clinic.entities;

import com.bio.clinic.dtos.DadosCadastroMedico;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Table(name = "medicos")
@Entity(name = "Medico")
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    // --- CONSTRUTORES ---

    // 1. Construtor Vazio (Obrigatório para o Spring/JPA funcionar)
    public Medico() {
    }

    // 2. Construtor para criar a partir do DTO
    public Medico(DadosCadastroMedico dados) {
        // Alterado de dados.nome() para dados.getNome()
        this.nome = dados.getNome();
    }

    // --- MÉTODOS DE NEGÓCIO ---

    public void atualizarInformacoes(DadosCadastroMedico dados) {
        // Alterado de dados.nome() para dados.getNome()
        if (dados.getNome() != null) {
            this.nome = dados.getNome();
        }
    }

    // --- GETTERS E SETTERS (Necessários sem o Lombok) ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}