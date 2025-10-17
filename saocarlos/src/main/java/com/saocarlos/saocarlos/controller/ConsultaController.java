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

			Optional<Consulta> registro = consultaRepo.findById(id);
			if (registro.isEmpty())
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Não há consulta com essa ID"));
			
			Consulta consultaAtual = registro.get();

			Consulta consultaUpdate = new Consulta();
			
			consultaUpdate.setId(id);
			consultaUpdate.setDataConsulta(consultaDTO.getDataConsulta());
			consultaUpdate.setHoraConsulta(consultaDTO.getHoraConsulta());
			consultaUpdate.setStatus(consultaDTO.getStatus());

			
			if (!consultaDTO.getNomePaciente().equals(consultaAtual.getPaciente().getNome())) {
				Optional<Paciente> registroPaciente = pacienteService.buscarPorNome(consultaDTO.getNomePaciente());
				if (registroPaciente.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Novo paciente não encontrado"));
				Paciente novoPaciente = registroPaciente.get();
				System.out.println("PACIENTE NOVO: " + novoPaciente.getNome());
				consultaUpdate.setPaciente(novoPaciente);
			} else {
				consultaUpdate.setPaciente(consultaAtual.getPaciente());
			}
			
			if (!consultaDTO.getNomeMedico().equals(consultaAtual.getMedico().getNomeMedico())) {
				List<Medico> registroMedico = medicoService.buscarPorNome(consultaDTO.getNomeMedico());
				if (registroMedico.isEmpty())
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", "Novo médico não encontrado"));
				Medico novoMedico = medicoService.encontrarNomeNaLista(registroMedico, consultaDTO.getNomeMedico());
				System.out.println("MEDICO NOVO: " + novoMedico.getNomeMedico());
				consultaUpdate.setMedico(novoMedico);
			} else {
				consultaUpdate.setMedico(consultaAtual.getMedico());
			}
			   		
			Consulta resultado = consultaRepo.save(consultaUpdate);
						
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

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarConsulta(@PathVariable Long id) {
        try {
            Optional<Consulta> consulta = consultaRepo.findById(id);
            if (consulta.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Consulta não encontrada"));
            }

            consultaRepo.deleteById(id);
            return ResponseEntity.ok(Map.of("mensagem", "Consulta deletada com sucesso!"));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("erro", "Erro ao deletar consulta: " + e.getMessage()));
        }
   }
}

