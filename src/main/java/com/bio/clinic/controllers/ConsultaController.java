package com.bio.clinic.controllers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bio.clinic.dtos.DadosAgendamentoConsulta;
import com.bio.clinic.dtos.DadosCheckin;
import com.bio.clinic.entities.Consulta;
import com.bio.clinic.services.AgendaService;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {

    @Autowired
    private AgendaService service;

    @PostMapping
    public ResponseEntity agendar(@RequestBody DadosAgendamentoConsulta dados) {
        try {
            Consulta consulta = service.agendar(dados);
            return ResponseEntity.ok(consulta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping("/paciente/{id}")
    public ResponseEntity<List<Consulta>> listarPorPaciente(@PathVariable Long id) {
        var lista = service.listarConsultasDoPaciente(id);
        return ResponseEntity.ok(lista);
    }
    
    @GetMapping("/horarios")
    public ResponseEntity<List<LocalTime>> getHorariosDisponiveis(
            @RequestParam Long idMedico,
            @RequestParam LocalDate data) {
            
        List<LocalTime> disponiveis = service.listarHorariosDisponiveis(idMedico, data);
        return ResponseEntity.ok(disponiveis);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity cancelar(@PathVariable Long id) {
        try {
            service.cancelarConsulta(id);
            // Retorna 204 (No Content) que é o padrão para deletar com sucesso
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping("/checkin")
    public ResponseEntity realizarCheckin(@RequestBody DadosCheckin dados) {
        try {
            service.realizarCheckin(dados);
            return ResponseEntity.ok("Check-in realizado com sucesso! Presença confirmada.");
        } catch (Exception e) {
            // Retorna 400 (Bad Request) com a mensagem de erro (ex: Longe demais)
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}