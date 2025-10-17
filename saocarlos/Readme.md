# 🏥 Sistema de Gestão de Agendamentos Médicos – São Carlos

Este projeto foi desenvolvido como parte do **Desafio Final do Bootcamp ElasTech (SoulCode + PagBank)**.
O sistema tem como objetivo gerenciar **consultas médicas**, permitindo o **cadastro de pacientes, médicos e agendamentos** de forma simples e organizada.

---

## 🧠 Sumário

1. Objetivo
2. Tecnologias Utilizadas
3. Arquitetura do Projeto
4. Modelagem do Banco de Dados
5. Regras de Negócio
6. Endpoints Principais
7. Como Executar o Projeto
8. Exemplo de Uso
9. Equipe

---

## 🎯 Objetivo

O **Sistema de Gestão de Agendamentos Médicos – São Carlos** foi criado para facilitar o controle de consultas médicas em clínicas.
Com ele, é possível:

* Cadastrar pacientes e médicos;
* Agendar, atualizar ou cancelar consultas;
* Visualizar todas as consultas em uma tabela organizada;
* Manter integridade dos dados e evitar conflitos de horário.

---

## 🧮 Tecnologias Utilizadas

| Camada             | Tecnologia             |
| ------------------ | ---------------------- |
| **Back-end**       | Java 21 / Spring Boot  |
| **Banco de Dados** | MySQL                  |
| **ORM**            | JPA / Hibernate        |
| **Front-end**      | HTML, CSS e JavaScript |
| **IDE**            | Eclipse e VS Code      |
| **Testes**         | Postman e Reqbin       |
| **Gerenciamento**  | Git e GitHub           |

---

## 🧬 Arquitetura do Projeto

A aplicação segue o padrão de arquitetura em **camadas**:

```
Controller → Service → Repository → Model → Database
```

### Estrutura dos pacotes:

```
src/main/java/com/saocarlos/saocarlos
├── SaoCarlosApplication.java
├── controller 
├── dto 
├── model
├── repository
└── service
```

---

## 🗂️ Modelagem do Banco de Dados

O banco de dados **sao_carlos** possui **três tabelas principais**:
`pacientes`, `medicos` e `consultas`.

### 🧟‍♂️ Tabela: pacientes

| Campo           | Tipo         | Descrição           |
| --------------- | ------------ | ------------------- |
| id_paciente     | INT (PK)     | Identificador único |
| nome_paciente   | VARCHAR(100) | Nome completo       |
| cpf_paciente    | VARCHAR(14)  | CPF único           |

---

### ⚕️ Tabela: medicos

| Campo         | Tipo         | Descrição            |
| ------------- | ------------ | -------------------- |
| id_medico     | INT (PK)     | Identificador único  |
| nome_medico   | VARCHAR(100) | Nome completo        |
| especialidade | VARCHAR(50)  | Especialidade médica |

---

### 🗕️ Tabela: consultas

| Campo         | Tipo         | Descrição                            |
| ------------- | ------------ | ------------------------------------ |
| id_consulta   | INT (PK)     | Identificador da consulta            |
| data_consulta | DATE         | Data da consulta                     |
| hora_consulta | TIME         | Horário                              |
| status        | VARCHAR(50)  | Situação da consulta                 |
| id_paciente   | INT (FK)     | Relaciona com `pacientes`            |
| id_medico     | INT (FK)     | Relaciona com `medicos`              |

--- 

Script **Banco de dados**

```
CREATE DATABASE sao_carlos;

CREATE TABLE medicos ( 
id_medico INT AUTO_INCREMENT PRIMARY KEY, 
nome_medico VARCHAR(100) NOT NULL, 
especialidade VARCHAR(50)  NOT NULL
);

CREATE TABLE pacientes (
 id_paciente INT AUTO_INCREMENT PRIMARY KEY, 
nome_paciente VARCHAR(100) NOT NULL,
cpf_paciente VARCHAR(14) NOT NULL UNIQUE
);

CREATE TABLE consultas (
id_consulta INT AUTO_INCREMENT PRIMARY KEY,
data_consulta DATE NOT NULL,
hora_consulta TIME NOT NULL,
status VARCHAR(50) NOT NULL,
id_paciente INT NOT NULL,
id_medico INT NOT NULL,
FOREIGN KEY (id_paciente) REFERENCES pacientes(id_paciente),
FOREIGN KEY (id_medico) REFERENCES medicos(id_medico)
);

```
---

### 🔗 Relacionamentos

```
PACIENTES (1) ───< CONSULTAS >───(1) MEDICOS
```

---

## ⚖️ Regras de Negócio

| Regra | Descrição                                                                       |
| ----- | ------------------------------------------------------------------------------- |
| R1    | Um paciente não pode ter duas consultas no mesmo dia e horário                  |
| R2    | Um médico não pode ter duas consultas no mesmo dia e horário                    |
| R3    | A data da consulta não pode estar no passado                                    |
| R4    | CPF do paciente deve ser único                                                  |
| R5    | Um médico possui apenas uma especialidade                                       |
| R6    | Fluxo do status: Agendada → Aguardando → Em atendimento → Concluída / Cancelada |

---

## 🌐 Endpoints

| Método | Endpoint - Pacientes       | Descrição                |
| ------ | ----------------- | ------------------------ |
| GET    | `/pacientes`      | Lista todos os pacientes |
| GET    | `/pacientes/{id}` | Busca paciente por ID    |
| GET    | `/pacientes/buscarPaciente/{nome}` | Busca paciente por Nome    |
| GET    | `/pacientes/buscarPacienteCPF/{cpf}` | Busca paciente por CPF    |
| POST   | `/pacientes/adicionarPaciente`      | Cadastra novo paciente   |
| PUT    | `/pacientes/{id}` | Atualiza paciente        |
| DELETE | `/pacientes/{id}` | Remove paciente          |

| Método | Endpoint - Médicos     | Descrição                |
| ------ | ----------------- | ------------------------ |
| GET    | `/medicos/listarMedicos`        | Lista todos os médicos           |
| GET    | `/medicos/{id}`        | Busca médicos por ID            |
| GET    | `/medicos/buscarMedico/{nome}`        | Busca médicos por Nome            |
| GET    | `/medicos//buscarEspecialidade/{espec}`        | Busca médicos por Nome            |
| POST   | `/medicos/adicionarMedico`      | Adiciona novo médico    |
| PUT    | `/medicos/{id}` | Atualiza médico      |
| DELETE | `/medicos/{id}` | Remove médico          |

| Método | Endpoint - Consultas     | Descrição                |
| ------ | ----------------- | ------------------------ |
| GET    | `/consultas`        | Lista todas as consultas          |
| GET    | `/consultas/{id}`        | Busca consultas por ID            |
| GET    | `/consultas/paciente/{nome}`        | Busca consulta por Nome do Paciente           |
| GET    | `/consultas/medico/{nome}`        | Busca consulta por Nome do Médico         |
| GET   | `/consultas/especialidade/{especialidade}`      | Busca consulta por Especialidade    |
| GET   | `/consultas/status`      | Busca consulta por Status    |
| POST    | `/consultas`        | Adiciona nova consulta          |
| PUT    | `/consultas/{id}` | Atualiza consulta    |
| PUT    | `/consultas/{id}/status` | Atualiza status da consulta    |

---

## ▶️ Como Executar o Projeto

### Pré-requisitos:

* Java 21
* Maven
* MySQL
* Postman/ Reqbin

### Passos:

1. Clone o repositório:

   ```bash
   git clone https://github.com/SEU_USUARIO/sao_carlos.git
   ```
2. Configure o banco de dados no arquivo `application.properties`:

   ```properties
   spring.application.name=sao_carlos
   server.port=8080
   spring.datasource.url=jdbc:mysql://localhost:3306/sao_carlos
   spring.datasource.username=root
   spring.datasource.password=
   spring.jpa.hibernate.ddl-auto=none
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
   logging.level.org.hibernate.SQL=DEBUG
   ```
3. Execute o projeto:

   ```bash
   mvn spring-boot:run
   ```
4. Acesse:

   ```
   http://localhost:8080
   ```

---

## 🗾 Exemplo de Uso

### POST `/pacientes`

```json
{
  "nome": "Maria Souza",
  "cpf": "987.654.321-00",
}
```

### Resposta

```json
{
  "id": 1,
  "nome": "Maria Souza",
  "cpf": "987.654.321-00",
}
```

---

## 👩‍💻 Equipe

* Anajara 
* Camila Ribeiro
* Fernanda Bastos
* Juliana Chang
* Victoria Li

> Projeto desenvolvido durante o **Bootcamp ElasTech – SoulCode Academy**, em parceria com **PagBank**.

---

## 📚 Referências

* [Spring Boot Documentation](https://spring.io/projects/spring-boot)
* [MySQL Reference Manual](https://dev.mysql.com/doc)
* SoulCode Academy – Bootcamp ElasTech
