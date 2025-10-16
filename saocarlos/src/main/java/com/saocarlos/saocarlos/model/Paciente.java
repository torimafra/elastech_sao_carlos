package com.saocarlos.saocarlos.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pacientes")
public class Paciente {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_paciente")
	private Long id;
	
	@Column(name = "nome_paciente", nullable = false, length = 100)
	private String nome;
	
	@Column(name = "cpf_paciente", unique = true, nullable = false, length = 14)
	private String cpf;
	
	public Paciente() {}

	public Paciente(Long id, String nome, String cpf) {
		this.id = id;
		this.nome = nome;
		this.cpf = cpf;		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}
	
	

}