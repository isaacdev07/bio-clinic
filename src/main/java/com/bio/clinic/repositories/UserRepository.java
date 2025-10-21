package com.bio.clinic.repositories;

import com.bio.clinic.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Busca um usuário pelo CPF.
     * O Spring Data JPA cria a consulta automaticamente baseada no nome do método.
     * * @param cpf O CPF do usuário a ser buscado.
     * @return um Optional contendo o usuário, se encontrado, ou vazio caso contrário.
     */
    Optional<User> findByCpf(String cpf);

}