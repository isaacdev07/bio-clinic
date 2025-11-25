package com.bio.clinic.controllers;

import com.bio.clinic.dtos.DadosCadastroMedico;
import com.bio.clinic.entities.Medico;
import com.bio.clinic.repositories.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    public ResponseEntity<Medico> cadastrar(@RequestBody DadosCadastroMedico dados) {
        // Converte o DTO para Entidade usando aquele construtor que criamos na entidade Medico
        Medico medico = new Medico(dados);
        
        // Salva no banco de dados
        repository.save(medico);

        return ResponseEntity.ok(medico);
    }


    @GetMapping
    public ResponseEntity<List<Medico>> listar() {
        List<Medico> lista = repository.findAll();
        return ResponseEntity.ok(lista);
    }
}