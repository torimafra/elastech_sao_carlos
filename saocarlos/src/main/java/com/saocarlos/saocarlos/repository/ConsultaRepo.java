package com.saocarlos.saocarlos.repository;

import java.util.*;
import java.time.*;

import com.saocarlos.saocarlos.model.Consulta;
import com.saocarlos.saocarlos.model.enums.StatusConsulta;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface ConsultaRepo extends JpaRepository<Consulta, Long> {

    @Query("SELECT c FROM Consulta c WHERE LOWER(c.paciente.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Consulta> findByPacienteNome(@Param("nome") String nome);


    @Query("SELECT c FROM Consulta c WHERE LOWER(c.medico.nomeMedico) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<Consulta> findByMedicoNome(@Param("nome") String nome);


    @Query("SELECT c FROM Consulta c WHERE LOWER(c.medico.especialidade) LIKE LOWER(CONCAT('%', :esp, '%'))")
    List<Consulta> findByEspecialidade(@Param("esp") String especialidade);

    @Query("SELECT c FROM Consulta c WHERE c.medico.id = :idMedico AND c.dataConsulta = :data AND c.horaConsulta = :hora")
    List<Consulta> findConsultaExistente(@Param("idMedico") Long idMedico,
                                         @Param("data") LocalDate data,
                                         @Param("hora") LocalTime hora);


    List<Consulta> findByStatus(StatusConsulta status);


}