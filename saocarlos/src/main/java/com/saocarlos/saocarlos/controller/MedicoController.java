package com.saocarlos.saocarlos.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.*;

import com.saocarlos.saocarlos.model.Medico;
import com.saocarlos.saocarlos.dto.MedicoDTO;
import com.saocarlos.saocarlos.service.MedicoService;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

	@Autowired
	private MedicoService medicoService;
	
	@GetMapping("/listarMedicos")
	public List<Medico> listarMedicos() {
		return medicoService.listarTodos();
	}
		
	@GetMapping("/{id}")
	public Medico medicoPorId(@PathVariable("id") Long id) {
		return medicoService.buscarPorId(id).orElse(null);
	}
		
	@GetMapping("/buscarMedico/{nome}")
	public ResponseEntity<?> medicoPorNome(@PathVariable("nome") String nome_medico) {
		
		try {
			
			List<Medico> medico = medicoService.buscarPorNome(nome_medico);
			return ResponseEntity.status(HttpStatus.OK).body(medico);
			
		} catch (IllegalArgumentException err) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}
	}
	
	
	@GetMapping("/buscarEspecialidade/{espec}")
	public List<Medico> medicoPorEspec(@PathVariable("espec") String especialidade) {
		return medicoService.buscarPorEspecialidade(especialidade);
	}
			
	@PostMapping("/adicionarMedico")
	public ResponseEntity<?> cadastrarMedico(@Valid @RequestBody MedicoDTO medicoDTO) {
		
		try {
			
			Medico medico = new Medico();
			medico.setNomeMedico(medicoDTO.getNomeMedico());
			medico.setEspecialidade(medicoDTO.getEspecialidade());
			medicoService.salvar(medico);
			return ResponseEntity.status(HttpStatus.OK).body(medico);
			
		} catch (IllegalArgumentException err) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}
		
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> atualizarMedico(@PathVariable("id") Long id, @RequestBody MedicoDTO medicoDTO) {
		
		try {
			
			Medico medico = new Medico();
			medico.setId(id);
			medico.setNomeMedico(medicoDTO.getNomeMedico());
			medico.setEspecialidade(medicoDTO.getEspecialidade());
			medicoService.atualizar(medico);
			return ResponseEntity.status(HttpStatus.CREATED).body(medico);
			
		} catch (IllegalArgumentException err) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}

	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> excluirMedico(@PathVariable ("id") Long id) {
		
		try {
			
			medicoService.deletar(id);
			return ResponseEntity.status(HttpStatus.OK).body(null);
			
		} catch (IllegalArgumentException err) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}

	}
	
}
