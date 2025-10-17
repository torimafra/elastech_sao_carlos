package com.saocarlos.saocarlos.controller;

import com.saocarlos.saocarlos.dto.ConsultaDTO;
import com.saocarlos.saocarlos.model.enums.StatusConsulta;
import com.saocarlos.saocarlos.repository.ConsultaRepo;
import com.saocarlos.saocarlos.model.Consulta;
import com.saocarlos.saocarlos.model.Medico;
import com.saocarlos.saocarlos.model.Paciente;
import com.saocarlos.saocarlos.service.ConsultaService;
import com.saocarlos.saocarlos.service.PacienteService;
import com.saocarlos.saocarlos.service.MedicoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;
    
    @Autowired
    private ConsultaRepo consultaRepo;
    
    @Autowired
    private PacienteService pacienteService;
    
    @Autowired
    private MedicoService medicoService;
    
    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
    	
        try {
            return ResponseEntity.ok(consultaService.buscarPorId(id));
            
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
        
    // Listar todas as consultas
    @GetMapping
    public ResponseEntity<List<ConsultaDTO>> listarTodas() {
        return ResponseEntity.ok(consultaService.listarTodas());
    }

    //  Buscar por nome do paciente
    @GetMapping("/paciente/{nome}")
    public ResponseEntity<List<ConsultaDTO>> buscarPorPaciente(@PathVariable String nome) {
        return ResponseEntity.ok(consultaService.buscarPorPaciente(nome));
    }

    //  Buscar por nome do médico
    @GetMapping("/medico/{nome}")
    public ResponseEntity<List<ConsultaDTO>> buscarPorMedico(@PathVariable String nome) {
        return ResponseEntity.ok(consultaService.buscarPorMedico(nome));
    }

    //  Buscar por especialidade
    @GetMapping("/especialidade/{especialidade}")
    public ResponseEntity<List<ConsultaDTO>> buscarPorEspecialidade(@PathVariable String especialidade) {
        return ResponseEntity.ok(consultaService.buscarPorEspecialidade(especialidade));
    }
    
    @GetMapping("/status")
    public ResponseEntity<List<ConsultaDTO>> buscarPorStatus(@RequestParam StatusConsulta status) {
        return ResponseEntity.ok(consultaService.buscarPorStatus(status));
    }

    //  Agendar nova consulta
    @PostMapping
    public ResponseEntity<?> agendarConsulta(@RequestBody ConsultaDTO dto) {
 
        try {
            ConsultaDTO consultaSalva = consultaService.agendarConsulta(dto); 
            return ResponseEntity.ok(consultaSalva);
            
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    // editar todos os campos de consulta
    @PutMapping("/editar/{id}")
    public ResponseEntity<?> editarConsulta(@PathVariable ("id") Long id, @RequestBody ConsultaDTO consultaDTO) {
    	try {
    		System.out.println("== UPDATE INFO ==");
    		System.out.println(consultaDTO.getId());
    		System.out.println(consultaDTO.getDataConsulta());
    		System.out.println(consultaDTO.getHoraConsulta());
    		System.out.println(consultaDTO.getStatus());
    		System.out.println(consultaDTO.getIdPaciente());
    		System.out.println(consultaDTO.getNomePaciente());
    		System.out.println(consultaDTO.getIdMedico());
    		System.out.println(consultaDTO.getNomeMedico());
    		System.out.println(consultaDTO.getEspecialidade());
			Optional<Consulta> registro = consultaRepo.findById(id);
			if (registro.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Não há consulta com essa ID"));
			
			Consulta consultaAtual = registro.get();
			System.out.println("== CONSULTA ATUAL ==");
    		System.out.println(consultaAtual.getId());
    		System.out.println(consultaAtual.getDataConsulta());
    		System.out.println(consultaAtual.getHoraConsulta());
    		System.out.println(consultaAtual.getStatus());
    		System.out.println(consultaAtual.getPaciente().getId());
    		System.out.println(consultaAtual.getPaciente().getNome());
    		System.out.println(consultaAtual.getMedico().getId());
    		System.out.println(consultaAtual.getMedico().getNomeMedico());
    		System.out.println(consultaAtual.getMedico().getEspecialidade());

			Consulta consultaUpdate = new Consulta();
			
			consultaUpdate.setId(id);
			consultaUpdate.setDataConsulta(consultaDTO.getDataConsulta());
			consultaUpdate.setHoraConsulta(consultaDTO.getHoraConsulta());
			consultaUpdate.setStatus(consultaDTO.getStatus());

			
			if (!consultaDTO.getNomePaciente().equals(consultaAtual.getPaciente().getNome())) {
				System.out.println("PACIENTES DIFERENTES");
				Optional<Paciente> registroPaciente = pacienteService.buscarPorNome(consultaDTO.getNomePaciente());
				if (registroPaciente.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Novo paciente não encontrado"));
				Paciente novoPaciente = registroPaciente.get();
				System.out.println("PACIENTE NOVO: " + novoPaciente.getNome());
				consultaUpdate.setPaciente(novoPaciente);
			}
			
			if (!consultaDTO.getNomeMedico().equals(consultaAtual.getMedico().getNomeMedico())) {
				Optional<Medico> registroMedico = medicoService.buscarPorNome(consultaDTO.getNomeMedico());
				if (registroMedico.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Novo médico não encontrado"));
				Medico novoMedico = registroMedico.get();
				System.out.println("MEDICO NOVO: " + novoMedico.getNomeMedico());
				consultaUpdate.setMedico(novoMedico);
			}
			
			System.out.println("== CONSULTA PARA MANDAR ==");
			System.out.println(consultaUpdate.getId());
    		System.out.println(consultaUpdate.getDataConsulta());
    		System.out.println(consultaUpdate.getHoraConsulta());
    		System.out.println(consultaUpdate.getStatus());
    		System.out.println(consultaUpdate.getPaciente().getId());
    		System.out.println(consultaUpdate.getPaciente().getNome());
    		System.out.println(consultaUpdate.getMedico().getId());
    		System.out.println(consultaUpdate.getMedico().getNomeMedico());
    		System.out.println(consultaUpdate.getMedico().getEspecialidade());
    		
			Consulta resultado = consultaRepo.save(consultaUpdate);
			
			System.out.println("== RESULTADO ==");
			System.out.println(resultado.getId());
    		System.out.println(resultado.getDataConsulta());
    		System.out.println(resultado.getHoraConsulta());
    		System.out.println(resultado.getStatus());
    		System.out.println(resultado.getPaciente().getId());
    		System.out.println(resultado.getPaciente().getNome());
    		System.out.println(resultado.getMedico().getId());
    		System.out.println(resultado.getMedico().getNomeMedico());
    		System.out.println(resultado.getMedico().getEspecialidade());
			
			return ResponseEntity.ok().body(resultado);
			
		} catch (IllegalArgumentException err) {
			 return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", err.getMessage()));
		}

    }
    
    // editar data e hora da consulta
    @PutMapping("/{id}")
    public ResponseEntity<?> reagendarConsulta(@PathVariable ("id") Long id, @RequestBody Consulta consultaAtualizada) {
        try {
            return ResponseEntity.ok(consultaService.reagendarConsulta(id, consultaAtualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //  Atualizar status (CANCELAR / CONCLUIR)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable ("id") Long id, @RequestParam StatusConsulta novoStatus) {
        try {
            return ResponseEntity.ok(consultaService.atualizarStatus(id, novoStatus));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

