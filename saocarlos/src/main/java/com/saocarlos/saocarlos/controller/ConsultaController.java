package com.saocarlos.saocarlos.controller;

import com.saocarlos.saocarlos.dto.ConsultaDTO;
import com.saocarlos.saocarlos.model.enums.StatusConsulta;
import com.saocarlos.saocarlos.model.Consulta;

import com.saocarlos.saocarlos.service.ConsultaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
@CrossOrigin(origins = "*")
public class ConsultaController {

    @Autowired
    private ConsultaService consultaService;
 
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

    // editar data e hora da consulta
    @PutMapping("/{id}")
    public ResponseEntity<?> reagendarConsulta(@PathVariable Long id, @RequestBody Consulta consultaAtualizada) {
        try {
            return ResponseEntity.ok(consultaService.reagendarConsulta(id, consultaAtualizada));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //  Atualizar status (CANCELAR / CONCLUIR)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam StatusConsulta novoStatus) {
        try {
            return ResponseEntity.ok(consultaService.atualizarStatus(id, novoStatus));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

