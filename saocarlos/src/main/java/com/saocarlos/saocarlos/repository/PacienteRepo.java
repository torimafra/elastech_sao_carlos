package com.saocarlos.saocarlos.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saocarlos.saocarlos.model.Paciente;

public interface PacienteRepo extends JpaRepository<Paciente, Long> {
	
	boolean existsByCpf(String cpf);
    Optional<Paciente> findByCpf(String cpf);
    Optional<Paciente> findByNomeContainingIgnoreCase(String nome);

}
