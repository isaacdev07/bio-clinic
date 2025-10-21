package com.bio.clinic.dtos;

// (Não precisa de validação aqui, pois a validação será feita pela autenticação)
public class LoginDTO {
    private String cpf;
    private String senha;

    // Construtor, Getters e Setters
    public LoginDTO() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}