package com.bio.clinic.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bio.clinic.dtos.DadosCadastroMedico;
import com.bio.clinic.entities.Medico;
import com.bio.clinic.repositories.MedicoRepository;


@Service
public class MedicoService {

    @Autowired
    private MedicoRepository repository;

    @Transactional
    public Medico cadastrar(DadosCadastroMedico dados) {
        
        Medico medico = new Medico(dados);
        return repository.save(medico);
    }

    public List<Medico> listarTodos() {
        // Aqui poderiam ser adicionadas regras de paginação ou filtros
        return repository.findAll();
    }
}