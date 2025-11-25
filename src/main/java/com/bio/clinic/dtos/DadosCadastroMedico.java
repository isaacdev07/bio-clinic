package com.bio.clinic.dtos;

public class DadosCadastroMedico {

    private String nome;

    // 1. Construtor vazio (Obrigatório para o Spring receber o JSON)
    public DadosCadastroMedico() {
    }

    // 2. Construtor completo (Opcional, mas útil para testes)
    public DadosCadastroMedico(String nome) {
        this.nome = nome;
    }

    // 3. Getter (Para sua entidade ler o valor)
    public String getNome() {
        return nome;
    }

    // 4. Setter (Para o Spring preencher o valor vindo do JSON)
    public void setNome(String nome) {
        this.nome = nome;
    }
}