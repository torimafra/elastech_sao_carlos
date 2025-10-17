package com.saocarlos.saocarlos.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;

import com.saocarlos.saocarlos.dto.PacienteInputDTO;
import com.saocarlos.saocarlos.model.Paciente;
import com.saocarlos.saocarlos.service.PacienteService;

@RestController
@RequestMapping("/pacientes") 
public class PacienteController {
	
	@Autowired
	private PacienteService pacienteService;
	
	@GetMapping("/{id}")
	public Optional<Paciente> buscarPorId(@PathVariable("id") Long id) {
		return pacienteService.buscarPorId(id);
	}
	
	@GetMapping("/buscarPaciente/{nome}")
	public Optional<Paciente> pacientePorNome(@PathVariable("nome") String nome_paciente) {
		return pacienteService.buscarPorNome(nome_paciente);
	}
	
	@GetMapping("/listarPacientes")
	public List<Paciente> listarTodos() {
		return pacienteService.listarTodos();
	}
	
	@GetMapping("/buscarPacienteCpf/{cpf}")
	public Optional<Paciente> pacientePorCpf(@PathVariable("cpf") String cpf_paciente) {
		return pacienteService.buscarPorCpf(cpf_paciente);
	}
	
	@PostMapping("/adicionarPaciente")
	public ResponseEntity<?> cadastrarPaciente(@Valid @RequestBody PacienteInputDTO pacienteInputDTO) {
		
		try {
			
		Paciente paciente = new Paciente();
	    paciente.setNome(pacienteInputDTO.getNome());
		paciente.setCpf(pacienteInputDTO.getCpf());
		pacienteService.salvar(paciente);
		return ResponseEntity.status(HttpStatus.OK).body(paciente);
		
		} catch (IllegalArgumentException err) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}
}
		
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarPaciente(@PathVariable("id") Long id, @RequestBody PacienteInputDTO 	pacienteInputDTO) {
			
		try {
			
            Paciente paciente = new Paciente();
            paciente.setId(id); 
            paciente.setNome(pacienteInputDTO.getNome()); 
            paciente.setCpf(pacienteInputDTO.getCpf());
            pacienteService.salvar(paciente); 
            return ResponseEntity.status(HttpStatus.CREATED).body(paciente);
				
		} catch (IllegalArgumentException err) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}

	}
		
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirPaciente(@PathVariable ("id") Long id) {
		
		try {
			
			pacienteService.deletar(id);
			return ResponseEntity.status(HttpStatus.OK).body(null);
			
		} catch (IllegalArgumentException err) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}

		
	}
	
	
	
	
}

