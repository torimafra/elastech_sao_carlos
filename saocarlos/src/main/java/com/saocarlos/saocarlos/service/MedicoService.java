package com.saocarlos.saocarlos.service;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

import com.saocarlos.saocarlos.model.Medico;
import com.saocarlos.saocarlos.repository.MedicoRepo;

@Service
public class MedicoService {
	
	@Autowired
	private MedicoRepo repository;
	
	public Medico encontrarNomeNaLista(List<Medico> lista, String nome) {
		
		for (int i = 0; i < lista.size(); i++) {
			if (lista.get(i).getNomeMedico().equalsIgnoreCase(nome)) {
				return lista.get(i);
			}
		}
		return null;
	}
	
	// Checa se já existe registro com mesmo nome e especialidade
	// Se houver, retorna exceção
	// Se não, cria registro
	public Medico salvar(Medico medico) {
		
		List<Medico> registroBanco = buscarPorNome(medico.getNomeMedico());
		
		if (!registroBanco.isEmpty()) {
			
			Medico medicoRegistrado = encontrarNomeNaLista(registroBanco, medico.getNomeMedico());
			
			if (medicoRegistrado.getEspecialidade().equals(medico.getEspecialidade())) {
				
				throw new IllegalArgumentException("Erro: Registro já existe no banco");
			}
		}

		return repository.save(medico);
	}
	
	// Checa se existe um registro com a id passada para atualização
	// Se não houver, retorna exceção
	// Se houver, atualiza
	public Medico atualizar(Medico medico) {
		
		Optional<Medico> registroBanco = buscarPorId(medico.getId());
		
		if (registroBanco.isEmpty()) {
			
			throw new IllegalArgumentException("Erro: Não há registro para atualizar");
		}
			
		return repository.save(medico);
	}
	
	public Optional<Medico> buscarPorId(Long id) {
		return repository.findById(id);
	}
	
	public List<Medico> listarTodos() {
		return repository.findAll();
	}
	
	// Checa se existe um registro com a id passada para ser deletada
	// Se não houver, retorna exceção
	// Se houver, deleta
	public void deletar(Long id) {
		
		Medico registroBanco = buscarPorId(id).orElse(null);
		
		if (registroBanco == null) {
			
			throw new IllegalArgumentException("Erro: Não há registro para deletar");
		}
		repository.deleteById(id);
	}
	
	
	public List<Medico> buscarPorNome(String nome_medico) {
		return repository.findByNomeMedicoContainingIgnoreCase(nome_medico);
	}
	
	
	public List<Medico> buscarPorEspecialidade(String especialidade) {
		return repository.findByEspecialidadeContainingIgnoreCase(especialidade);
	}

}
