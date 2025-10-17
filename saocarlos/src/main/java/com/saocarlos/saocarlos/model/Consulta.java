package com.saocarlos.saocarlos.model;

import jakarta.persistence.*;
import java.time.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.saocarlos.saocarlos.model.enums.StatusConsulta;

@Entity
@Table(name = "consultas")
public class Consulta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_consulta")
    private Long id;

    @Column(name = "data_consulta", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataConsulta;

    @Column(name = "hora_consulta", nullable = false, columnDefinition = "TIME")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaConsulta;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusConsulta status;

    @ManyToOne
    @JoinColumn(name = "id_paciente", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "id_medico", nullable = false)
    private Medico medico;

    // Construtor vazio (necessário para o JPA)
    public Consulta() {}

    // Construtor completo
    public Consulta(Long id, LocalDate dataConsulta, LocalTime horaConsulta, StatusConsulta status,
                    Paciente paciente, Medico medico) {
        this.id = id;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
        this.status = status;
        this.paciente = paciente;
        this.medico = medico;
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

    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }

    public Medico getMedico() { return medico; }
    public void setMedico(Medico medico) { this.medico = medico; }
}