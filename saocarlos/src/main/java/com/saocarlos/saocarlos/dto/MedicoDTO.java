package com.saocarlos.saocarlos.dto;

import jakarta.validation.constraints.*;

public class MedicoDTO {
	
	@NotBlank(message = "Campo obrigatório; não pode conter apenas espaços.")
	@Size(min = 5, message = "O nome deve conter no mínimo cinco caracteres.")
	private String nomeMedico;
	
	@NotBlank(message = "Campo obrigatório; não pode conter apenas espaços.")
	@Size(min = 5, message = "A especialidade deve conter no mínimo cinco caracteres.")
	private String especialidade;
	
	public String getNomeMedico() {
		return nomeMedico;
	}

	public void setNome_medico(String nomeMedico) {
		this.nomeMedico = nomeMedico;
	}

	public String getEspecialidade() {
		return especialidade;
	}

	public void setEspecialidade(String especialidade) {
		this.especialidade = especialidade;
	}
	
}
