package com.saocarlos.saocarlos.dto;

import java.time.*;

import com.saocarlos.saocarlos.model.enums.StatusConsulta;

public class ConsultaDTO {

    private Long id;
    private LocalDate dataConsulta;
    private LocalTime horaConsulta;
    private StatusConsulta status;

    private Long idPaciente;
    private String nomePaciente;

    private Long idMedico;
    private String nomeMedico;
    private String especialidade;

    // Construtores
    public ConsultaDTO() {}

    public ConsultaDTO(Long id, LocalDate dataConsulta, LocalTime horaConsulta, StatusConsulta status,
                       Long idPaciente, String nomePaciente,
                       Long idMedico, String nomeMedico, String especialidade) {
        this.id = id;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
        this.status = status;
        this.idPaciente = idPaciente;
        this.nomePaciente = nomePaciente;
        this.idMedico = idMedico;
        this.nomeMedico = nomeMedico;
        this.especialidade = especialidade;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDate dataConsulta) { this.dataConsulta = dataConsulta; }

    public LocalTime getHoraConsulta() { return horaConsulta; }
    public void setHoraConsulta(LocalTime horaConsulta) { this.horaConsulta = horaConsulta; }

    public StatusConsulta getStatus() { return status; }
    public void setStatus(StatusConsulta status) { this.status = status; }

    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }

    public String getNomePaciente() { return nomePaciente; }
    public void setNomePaciente(String nomePaciente) { this.nomePaciente = nomePaciente; }

    public Long getIdMedico() { return idMedico; }
    public void setIdMedico(Long idMedico) { this.idMedico = idMedico; }

    public String getNomeMedico() { return nomeMedico; }
    public void setNomeMedico(String nomeMedico) { this.nomeMedico = nomeMedico; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }
}

