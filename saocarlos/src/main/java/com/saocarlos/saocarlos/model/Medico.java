package com.saocarlos.saocarlos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "medicos")
public class Medico {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_medico")
	private Long id;
	
	@Column(name = "nome_medico",  length = 100, nullable = false)
	private String nomeMedico;
	
	@Column(name = "especialidade", length = 50, nullable = false)
	private String especialidade;
	
	public Medico() {};
	
	public Medico(Long id, String nomeMedico, String especialidade) {
		this.id = id;
		this.nomeMedico = nomeMedico;
		this.especialidade = especialidade;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getNomeMedico() {
		return nomeMedico;
	}

	public void setNomeMedico(String nomeMedico) {
		this.nomeMedico = nomeMedico;
	}
	
	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}

}
