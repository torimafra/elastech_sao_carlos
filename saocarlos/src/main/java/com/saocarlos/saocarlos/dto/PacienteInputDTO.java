package com.saocarlos.saocarlos.dto;

import jakarta.validation.constraints.*;

public class PacienteInputDTO {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "(\\d{11})|(\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})",
    message = "CPF deve ser 11 dígitos ou no formato 000.000.000-00")
    private String cpf;

    public String getNome() { return nome; }
    
    public void setNome(String nome) { this.nome = nome; }
    
    public String getCpf() { return cpf; }
    
    public void setCpf(String cpf) { this.cpf = cpf; }
}