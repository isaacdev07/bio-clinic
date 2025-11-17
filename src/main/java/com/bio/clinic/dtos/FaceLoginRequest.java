package com.bio.clinic.dtos;

public class FaceLoginRequest {
    private String cpf;

    // Construtor, Getter e Setter
    public FaceLoginRequest() {
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}