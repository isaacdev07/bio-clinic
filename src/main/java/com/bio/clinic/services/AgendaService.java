package com.bio.clinic.services;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bio.clinic.dtos.DadosAgendamentoConsulta;
import com.bio.clinic.dtos.DadosCheckin;
import com.bio.clinic.entities.Consulta;
import com.bio.clinic.entities.Medico;
import com.bio.clinic.repositories.ConsultaRepository;
import com.bio.clinic.repositories.MedicoRepository;
import com.bio.clinic.utils.GeoUtils;

@Service
public class AgendaService {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    /**
     * Método para agendar uma consulta.
     * Verifica se o médico existe e se o horário está livre.
     */
    public Consulta agendar(DadosAgendamentoConsulta dados) {
        // 1. Busca o médico
        if (!medicoRepository.existsById(dados.getIdMedico())) {
            throw new RuntimeException("Id do médico informado não existe!");
        }
        Medico medico = medicoRepository.findById(dados.getIdMedico()).get();

        // 2. VALIDAÇÃO: Verifica se o horário já está ocupado
        boolean horarioOcupado = consultaRepository.existsByMedicoAndDataHora(medico, dados.getDataHora());
        
        if (horarioOcupado) {
            throw new RuntimeException("Médico já possui outra consulta agendada neste horário!");
        }

        // 3. Cria a consulta
        Consulta consulta = new Consulta();
        consulta.setMedico(medico);
        consulta.setPacienteId(dados.getIdPaciente());
        consulta.setDataHora(dados.getDataHora());
        consulta.setTipoConsulta(dados.getTipoConsulta());

        // 4. Salva no banco
        return consultaRepository.save(consulta);
    }

    /**
     * Lista o histórico de consultas de um paciente.
     */
    public List<Consulta> listarConsultasDoPaciente(Long idPaciente) {
        return consultaRepository.findByPacienteId(idPaciente);
    }

    /**
     * NOVO: Lista apenas os horários disponíveis para um médico em uma data específica.
     * Regra de negócio: Consultas de 1h em 1h, das 08:00 às 18:00.
     */
    public List<LocalTime> listarHorariosDisponiveis(Long idMedico, LocalDate data) {
        
        // Valida se médico existe antes de processar
        if (!medicoRepository.existsById(idMedico)) {
             throw new RuntimeException("Médico não encontrado!");
        }

        // 1. Busca todas as consultas ocupadas naquele dia (00:00 até 23:59)
        // Nota: Certifique-se de ter adicionado o método findByMedicoIdAndDataHoraBetween no Repository
        List<Consulta> agendamentos = consultaRepository.findByMedicoIdAndDataHoraBetween(
                idMedico, 
                data.atStartOfDay(), 
                data.atTime(23, 59, 59)
        );

        // 2. Extrai apenas a HORA dos agendamentos (Ignora data e outros dados)
        List<LocalTime> horariosOcupados = agendamentos.stream()
                .map(consulta -> consulta.getDataHora().toLocalTime())
                .collect(Collectors.toList());

        // 3. Calcula os horários livres (Das 08:00 as 18:00)
        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        
        for (int hora = 8; hora < 18; hora++) {
            LocalTime horarioAnalise = LocalTime.of(hora, 0); // Cria 08:00, 09:00...

            // Se NÃO estiver na lista de ocupados, adiciona na lista de disponíveis
            if (!horariosOcupados.contains(horarioAnalise)) {
                horariosDisponiveis.add(horarioAnalise);
            }
        }

        return horariosDisponiveis;
    }
    
    public void cancelarConsulta(Long idConsulta) {
        if (!consultaRepository.existsById(idConsulta)) {
            throw new RuntimeException("Consulta não encontrada!");
        }
        consultaRepository.deleteById(idConsulta);
    }
    

    private static final double LAT_CLINICA = -23.64826;
    private static final double LON_CLINICA = -46.72211;
    

    private static final double DISTANCIA_MAXIMA_METROS = 200.0;

    public void realizarCheckin(DadosCheckin dados) {
        // 1. Busca a consulta
        Consulta consulta = consultaRepository.findById(dados.getIdConsulta())
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));

        // 2. Calcula a distância entre o Paciente e a Clínica
        double distancia = GeoUtils.calcularDistanciaEmMetros(
                dados.getLatitude(), 
                dados.getLongitude(), 
                LAT_CLINICA, 
                LON_CLINICA
        );

        System.out.println("Distância do usuário: " + distancia + " metros.");

        // 3. Valida se está perto o suficiente
        if (distancia > DISTANCIA_MAXIMA_METROS) {
            throw new RuntimeException("Check-in recusado! Você está a " + (int)distancia + 
                                     "m da clínica. Aproxime-se para confirmar.");
        }

        
        // Por enquanto, só vamos logar ou retornar sucesso
    }
}