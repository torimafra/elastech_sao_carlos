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
    public ConsultaDTO agendarConsulta(Consulta consulta) {

        // 1️⃣ Verifica se o médico existe
        if (consulta.getMedico() == null || consulta.getMedico().getId() == null) {
            throw new RuntimeException("Médico não informado!");
        }
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new RuntimeException("Paciente não informado!");
        }


        Medico medico = medicoRepo.findById(consulta.getMedico().getId())
                .orElseThrow(() -> new RuntimeException("Médico não cadastrado!"));

        // 2️⃣ Verifica se o paciente está cadastrado
        Paciente paciente;
        if (consulta.getPaciente() == null || consulta.getPaciente().getId() == null) {
            throw new RuntimeException("Paciente não cadastrado! É necessário cadastrar o paciente antes de agendar a consulta.");
        } else {
            paciente = pacienteRepo.findById(consulta.getPaciente().getId())
                    .orElseThrow(() -> new RuntimeException("Paciente não encontrado no sistema!"));
        }

        // 3️⃣ Verifica disponibilidade do médico
        boolean disponivel = medicoDisponivel(
                consulta.getMedico().getId(),
                consulta.getDataConsulta(),
                consulta.getHoraConsulta()
        );



        if (!disponivel) {
            throw new RuntimeException("Médico já possui consulta neste horário!");
        }

        // 4️⃣ Salva a consulta
        consulta.setMedico(medico);
        consulta.setPaciente(paciente);
        consulta.setStatus(StatusConsulta.AGENDADA);

        Consulta salva = consultaRepository.save(consulta);
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

