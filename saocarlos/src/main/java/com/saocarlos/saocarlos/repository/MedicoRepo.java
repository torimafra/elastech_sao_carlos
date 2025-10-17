package com.saocarlos.saocarlos.repository;

import java.util.*;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saocarlos.saocarlos.model.Medico;

public interface MedicoRepo extends JpaRepository<Medico, Long> {

	List<Medico> findByNomeMedicoContainingIgnoreCase(String nome_medico);
	List<Medico> findByEspecialidadeContainingIgnoreCase(String especialidade);
}
