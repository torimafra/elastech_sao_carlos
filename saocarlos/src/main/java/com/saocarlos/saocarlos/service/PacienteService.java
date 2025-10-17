package com.saocarlos.saocarlos.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saocarlos.saocarlos.model.Paciente;
import com.saocarlos.saocarlos.repository.PacienteRepo;

@Service
public class PacienteService {
	
	@Autowired
	private PacienteRepo repository;
	
	public Paciente salvar(Paciente paciente) {
		return repository.save(paciente);
	}
	
	public Paciente atualizar(Paciente paciente) {
		return repository.save(paciente);
	}
	
	public Optional<Paciente> buscarPorId(Long id){
		return repository.findById(id);
	}
	
	public Optional<Paciente> buscarPorNome(String nome) {
	    return repository.findByNomeContainingIgnoreCase(nome);
	}
	
	public Optional<Paciente>buscarPorCpf(String cpf) {
		return repository.findByCpf(cpf);
	}
	
	public List<Paciente> listarTodos(){
		return repository.findAll();
	}
	
	public void deletar(Long id) {
		repository.deleteById(id);
	}
	
	public boolean cpfJaExiste(String cpf) {
        return repository.existsByCpf(cpf);
    }
}