package com.saocarlos.saocarlos.service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

import com.saocarlos.saocarlos.dto.ConsultaDTO;
import com.saocarlos.saocarlos.model.*;
import com.saocarlos.saocarlos.model.enums.StatusConsulta;
import com.saocarlos.saocarlos.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsultaService {

    @Autowired
    private ConsultaRepo consultaRepository;

    @Autowired
    private MedicoRepo medicoRepo;

    @Autowired
    private PacienteRepo pacienteRepo;

    // Converter entidade em DTO
    private ConsultaDTO converterParaDTO(Consulta consulta) {
        return new ConsultaDTO(
                consulta.getId(),
                consulta.getDataConsulta(),
                consulta.getHoraConsulta(),
                consulta.getStatus(),
                consulta.getPaciente().getId(),
                consulta.getPaciente().getNome(),
                consulta.getMedico().getId(),
                consulta.getMedico().getNomeMedico(),
                consulta.getMedico().getEspecialidade()
        );
    }

    public ConsultaDTO buscarPorId(Long id) {
        return consultaRepository.findById(id)
                .map(this::converterParaDTO)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));
    }
    
 // Supondo que você injetou MedicoRepository e PacienteRepository
    
    // Listar todas as consultas
    public List<ConsultaDTO> listarTodas() {
        return consultaRepository.findAll()
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar por paciente
    public List<ConsultaDTO> buscarPorPaciente(String nome) {
        return consultaRepository.findByPacienteNome(nome)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar por médico
    public List<ConsultaDTO> buscarPorMedico(String nome) {
        return consultaRepository.findByMedicoNome(nome)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    // Buscar por especialidade
    public List<ConsultaDTO> buscarPorEspecialidade(String especialidade) {
        return consultaRepository.findByEspecialidade(especialidade)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultaDTO> buscarPorStatus(StatusConsulta status) {
        return consultaRepository.findByStatus(status)
                .stream()
                .map(this::converterParaDTO)
                .collect(Collectors.toList());
    }
    
    // Verificar se médico está disponível
    public boolean medicoDisponivel(Long idMedico, LocalDate data, LocalTime hora) {
        return consultaRepository.findConsultaExistente(idMedico, data, hora).isEmpty();
    }

     // Agendar consulta
    public ConsultaDTO agendarConsulta(ConsultaDTO dto) {

        if (dto.getIdMedico() == null || dto.getIdPaciente() == null) {
             throw new RuntimeException("Médico e/ou Paciente não informados!");
        }

        Medico medico = medicoRepo.findById(dto.getIdMedico())
                .orElseThrow(() -> new RuntimeException("Médico não cadastrado!"));

        Paciente paciente = pacienteRepo.findById(dto.getIdPaciente())
                .orElseThrow(() -> new RuntimeException("Paciente não encontrado no sistema!"));

        boolean disponivel = medicoDisponivel(
                dto.getIdMedico(),
                dto.getDataConsulta(),
                dto.getHoraConsulta()
        );

        if (!disponivel) {
            throw new RuntimeException("Médico já possui consulta neste horário!");
        }

        Consulta novaConsulta = new Consulta();
        novaConsulta.setDataConsulta(dto.getDataConsulta());
        novaConsulta.setHoraConsulta(dto.getHoraConsulta());
        novaConsulta.setStatus(StatusConsulta.AGENDADA);
        
        novaConsulta.setMedico(medico);
        novaConsulta.setPaciente(paciente);
        
        Consulta salva = consultaRepository.save(novaConsulta);
        return converterParaDTO(salva);
    }

    // Atualizar status
    public ConsultaDTO atualizarStatus(Long id, StatusConsulta novoStatus) {
        Optional<Consulta> consultaOpt = consultaRepository.findById(id);

        if (consultaOpt.isEmpty()) {
            throw new RuntimeException("Consulta não encontrada!");
        }

        Consulta consulta = consultaOpt.get();
        consulta.setStatus(novoStatus);
        Consulta atualizada = consultaRepository.save(consulta);

        return converterParaDTO(atualizada);
    }

    // ✅ Reagendar consulta (alterar data e hora)
    public ConsultaDTO reagendarConsulta(Long id, Consulta consultaAtualizada) {
        Consulta consulta = consultaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada!"));

        // Verifica se o médico ainda está disponível no novo horário
        boolean disponivel = medicoDisponivel(
                consulta.getMedico().getId(),
                consultaAtualizada.getDataConsulta(),
                consultaAtualizada.getHoraConsulta()
        );

        if (!disponivel) {
            throw new RuntimeException("Médico já possui consulta neste novo horário!");
        }

        // Atualiza data e hora
        consulta.setDataConsulta(consultaAtualizada.getDataConsulta());
        consulta.setHoraConsulta(consultaAtualizada.getHoraConsulta());

        // Mantém o status como AGENDADA
        consulta.setStatus(StatusConsulta.AGENDADA);

        Consulta atualizada = consultaRepository.save(consulta);
        return converterParaDTO(atualizada);
    }

}