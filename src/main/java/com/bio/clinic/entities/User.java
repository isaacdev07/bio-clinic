package com.bio.clinic.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "tb_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "A senha é obrigatória")
    private String senha;

    @CPF(message = "CPF inválido")
    @NotBlank(message = "O CPF é obrigatório")
    @Column(unique = true, name = "cpf")
    private String cpf;
    
    // --- ADICIONADO ---
    // Define uma coluna com tamanho grande para o descritor facial.
    // 4000 é um bom tamanho. Se estourar, pode mudar para @Lob (Large Object).
    @Column(length = 4000)
    private String faceDescriptor;
    // --- FIM DA ADIÇÃO ---
    
    // --- CONSTRUTORES, GETTERS, SETTERS, ETC ---

    // Construtor vazio (requerido pelo JPA)
    public User() {
    }

    // Construtor para facilitar a criação de novos usuários
    // Não incluímos o faceDescriptor aqui, pois ele é cadastrado DEPOIS
    public User(String nome, String senha, String cpf) {
        this.nome = nome;
        this.senha = senha;
        this.cpf = cpf;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }


    public String getFaceDescriptor() { return faceDescriptor; }
    public void setFaceDescriptor(String faceDescriptor) { this.faceDescriptor = faceDescriptor; }
    

    // --- Métodos da interface UserDetails ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }
    @Override
    public String getPassword() { return this.senha; }
    @Override
    public String getUsername() { return this.cpf; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }

    // equals() e hashCode() (corretos, não precisam do faceDescriptor)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(cpf, user.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cpf);
    }
}