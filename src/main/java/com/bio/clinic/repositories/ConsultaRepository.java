package com.bio.clinic.repositories;

import com.bio.clinic.entities.Consulta;
import com.bio.clinic.entities.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ConsultaRepository extends JpaRepository<Consulta, Long> {

    // Lista todas as consultas de um paciente
    List<Consulta> findByPacienteId(Long pacienteId);

    // Retorna true se ocupado, false se livre
    boolean existsByMedicoAndDataHora(Medico medico, LocalDateTime dataHora);
    
    List<Consulta> findByMedicoIdAndDataHoraBetween(Long medicoId, LocalDateTime inicio, LocalDateTime fim);
    
}