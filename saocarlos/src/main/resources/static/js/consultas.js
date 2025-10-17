// 📡 Configuração da API
const API_BASE = 'http://localhost:8080/consultas';

/* ==========================
   DETECTAR PÁGINA ATUAL
========================== */
window.onload = async function () {
  const page = window.location.pathname.split('/').pop();

  switch (page) {
    case 'medicos.html':
      await listarMedicos();
      break;
    case 'pacientes.html':
      await listarPacientes();
      break;
    case 'consultas.html':
      await listarConsultas();
	  await carregarSelects();
      break;
  }
};

// 🎯 Elementos do DOM
const formularios = {
    agendar: document.getElementById('formAgendar'),
    atualizar: document.getElementById('formAtualizar'),
    deletar: document.getElementById('formDeletar'),
    listar: document.getElementById('formListar')
};

// 🏠 Funções de Navegação
function voltarMenu() {
    Object.values(formularios).forEach(form => form.classList.remove('ativo'));
    limparTodosResultados();
}

function mostrarAgendar() {
    voltarMenu();
    formularios.agendar.classList.add('ativo');
	console.log("LINHA 40");
    carregarSelects();
}

function mostrarAtualizar() {
    voltarMenu();
    formularios.atualizar.classList.add('ativo');
	console.log("LNHA 47");
    carregarSelects();
}

function mostrarDeletar() {
    voltarMenu();
    formularios.deletar.classList.add('ativo');
}

function mostrarListar() {
    voltarMenu();
    formularios.listar.classList.add('ativo');
    listarConsultas();
}

// 🧹 Funções de Limpeza
function limparTodosResultados() {
    document.getElementById('resultadoAgendar').innerHTML = '';
    document.getElementById('resultadoAtualizar').innerHTML = '';
    document.getElementById('resultadoDeletar').innerHTML = '';
    document.getElementById('resultadoListar').innerHTML = '';
}

function limparFormAgendar() {
    document.getElementById('dataConsulta').value = '';
    document.getElementById('selectMedico').value = '';
    document.getElementById('selectPaciente').value = '';
    document.getElementById('selectHorario').innerHTML = '';
    document.getElementById('resultadoAgendar').innerHTML = '';
}

function limparFormAtualizar() {
    document.getElementById('idConsultaAtualizar').value = '';
    document.getElementById('dataConsultaAtualizar').value = '';
    document.getElementById('selectMedicoAtualizar').value = '';
    document.getElementById('selectPacienteAtualizar').value = '';
    document.getElementById('selectHorarioAtualizar').innerHTML = '<option value="">Selecione o horário</option>';
    document.getElementById('resultadoAtualizar').innerHTML = '';
}

function limparFormDeletar() {
    document.getElementById('idDeletar').value = '';
    document.getElementById('resultadoDeletar').innerHTML = '';
}

// 🛠️ Funções Utilitárias
function mostrarMensagem(elemento, mensagem, tipo) {
    elemento.innerHTML = `
        <div class="mensagem ${tipo}">
            ${mensagem}
        </div>
    `;
}

function mostrarLoading(elemento) {
    elemento.innerHTML = `
        <div class="loading">
            ⏳ Processando...
        </div>
    `;
}

function formatarData(dataString) {
    if (!dataString) return 'Não informada';
    const data = new Date(dataString);
    return data.toLocaleDateString('pt-BR');
}

// 📋 Carregar selects de médicos e pacientes
async function carregarSelects() {
    const medicosSelects = document.querySelectorAll('select[id^="selectMedico"]');
    const pacientesSelects = document.querySelectorAll('select[id^="selectPaciente"]');

    try {
        const [medicosRes, pacientesRes] = await Promise.all([
            fetch('http://localhost:8080/medicos/listarMedicos'),
            fetch('http://localhost:8080/pacientes/listarPacientes')
        ]);

        const medicos = await medicosRes.json();
        const pacientes = await pacientesRes.json();

        medicosSelects.forEach(sel => {
            sel.innerHTML = '<option value="">Selecione o médico</option>';
            medicos.forEach(m => sel.innerHTML += `<option value="${m.id}">${m.nomeMedico}</option>`);
        });

        pacientesSelects.forEach(sel => {
            sel.innerHTML = '<option value="">Selecione o paciente</option>';
            pacientes.forEach(p => sel.innerHTML += `<option value="${p.id}">${p.nome}</option>`);
        });

    } catch (erro) {
        console.error('Erro ao carregar selects:', erro);
    }
}

// 🔍 FILTRAR CONSULTAS POR MÉDICO OU PACIENTE
//async function filtrarConsultas() {
//    const filtro = document.getElementById('inputFiltro').value.toLowerCase();
//    const resultadoDiv = document.getElementById('resultadoListar');
//    mostrarLoading(resultadoDiv);
//
//    try {
//        const resposta = await fetch(`${API_BASE}`);
//		console.log("LINHA 153");
//        if (!resposta.ok) throw new Error('Erro ao buscar consultas.');
//
//        const consultas = await resposta.json();
//
//        // Filtra por médico ou paciente
//        const filtradas = consultas.filter(c =>
//            c.nome_medico.toLowerCase().includes(filtro) ||
//            c.nome_paciente.toLowerCase().includes(filtro)
//        );
//
//        if (!filtradas.length) {
//            mostrarMensagem(resultadoDiv, '⚠️ Nenhuma consulta encontrada com este filtro.', 'info');
//            return;
//        }
//
//        renderizarTabelaConsultas(filtradas, resultadoDiv);
//
//    } catch (erro) {
//        mostrarMensagem(resultadoDiv, `❌ ${erro.message}`, 'erro');
//    }
//}

// Função auxiliar para exibir feedback na tabela
function exibirMensagemNaTabela(mensagem, colspan = 4) { 
    const tbody = document.getElementById('tabelaConsultas');
    if (tbody) {
        tbody.innerHTML = `
            <tr><td colspan="${colspan}" style="text-align:center;">${mensagem}</td></tr>
        `;
    }
}

async function filtrarConsultas() {
	const tipoFiltro = document.getElementById('tipoFiltro').value;
    const valorFiltro = document.getElementById('filtro').value.toLowerCase().trim();
	let filtradas;


    try {
        const resposta = await fetch(`${API_BASE}`);

        if (!resposta.ok) throw new Error('Erro ao buscar consultas.');

        const consultas = await resposta.json();
		
		console.log(consultas);
		console.log(tipoFiltro);
		console.log(valorFiltro);
		
		switch (tipoFiltro) {
			case 'id':
				filtradas = consultas.filter(c =>
					c.id.toString() === valorFiltro);
				break;
			case 'nome':
				filtradas = consultas.filter(c =>
				    c.nomeMedico.toLowerCase().includes(valorFiltro));
				break;
			case 'status':
				filtradas = consultas.filter(c =>
					c.status.toLowerCase().includes(valorFiltro));
		}
		
		console.log(filtradas);
        // Filtra por médico ou paciente
        //const filtradas = consultas.filter(c =>
        //    c.nomeMedico.toLowerCase().includes(valorFiltro) ||
        //    c.nomePaciente.toLowerCase().includes(valorFiltro)
        //);

        if (!filtradas.length) {
			exibirMensagemNaTabela('⚠️ Nenhuma consulta encontrada com este filtro.');
            //mostrarMensagem(resultadoDiv, '⚠️ Nenhuma consulta encontrada com este filtro.', 'info');
            return;
        }

        renderizarConsultas(filtradas);

    } catch (erro) {
		console.error('Erro ao filtrar consultas:', erro);
		exibirMensagemNaTabela('❌ Erro ao buscar consultas.');
        //mostrarMensagem(resultadoDiv, `❌ ${erro.message}`, 'erro');
    }
}

// ➕ AGENDAR CONSULTA
async function agendarConsulta(event) {
	
	event.preventDefault();
	
    const data = document.getElementById('dataConsulta').value;
    const hora = document.getElementById('selectHorario').value;
    const medico = document.getElementById('selectMedico').value;
    const paciente = document.getElementById('selectPaciente').value;
    const resultadoDiv = document.getElementById('resultadoAgendar');

    if (!data || !hora || !medico || !paciente) {
        mostrarMensagem(resultadoDiv, '❌ Preencha todos os campos.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
		const consultaDTO = { 
		    dataConsulta: data, 
		    horaConsulta: hora, 
		    status: "AGENDADA", // Use o valor do ENUM em maiúsculas se o Java for sensível
		    idPaciente: paciente, 
		    idMedico: medico,
		    
		    // CAMPOS ADICIONAIS NECESSÁRIOS PARA O DTO DE JAVA NÃO FALHAR
		    nomePaciente: "",      // Enviar como string vazia
		    nomeMedico: "",        // Enviar como string vazia
		    especialidade: ""      // Enviar como string vazia
		};
        const resposta = await fetch(`${API_BASE}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(consultaDTO)
        });

        if (!resposta.ok) throw new Error('Erro ao agendar consulta.');

        const consulta = await resposta.json();

        mostrarMensagem(resultadoDiv, `✅ Consulta agendada com sucesso! ID: ${consulta.id}`, 'sucesso');

        await listarConsultas();

        setTimeout(limparFormAgendar, 3000);
    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ ${erro.message}`, 'erro');
    }
}

// 🔍 LISTAR TODAS AS CONSULTAS
//async function listarConsultas() {
//    const resultadoDiv = document.getElementById('resultadoListar');
//    mostrarLoading(resultadoDiv);
//
//    try {
//        const resposta = await fetch(`${API_BASE}`);
//		console.log("LINHA 244");
//        if (!resposta.ok) throw new Error('Erro ao buscar consultas.');
//
//        const consultas = await resposta.json();
//
//        if (!consultas.length) {
//            mostrarMensagem(resultadoDiv, '⚠️ Nenhuma consulta encontrada.', 'info');
//            return;
//        }
//
//        renderizarPacientes(pacientes);
//
//    } catch (erro) {
//        mostrarMensagem(resultadoDiv, `❌ ${erro.message}`, 'erro');
//    }
//}

// CARREGAR CONSULTAS
let allConsultas = [];

async function listarConsultas() {
    exibirMensagemNaTabela('⏳ Carregando consultas...');
    try {
        const resposta = await fetch(`${API_BASE}`);
        if (!resposta.ok) throw new Error('Erro ao carregar consultas');
        allConsultas = await resposta.json();
        renderizarConsultas(allConsultas);
    } catch (erro) {
        console.error(erro);
        exibirMensagemNaTabela('❌ Erro ao carregar consultas.');
    }
}

// ✏️ ATUALIZAR CONSULTA
async function atualizarConsulta() {
    const id = document.getElementById('idConsultaAtualizar').value;
    const data = document.getElementById('dataConsultaAtualizar').value;
    const hora = document.getElementById('selectHorarioAtualizar').value;
    const medico = document.getElementById('selectMedicoAtualizar').value;
    const paciente = document.getElementById('selectPacienteAtualizar').value;
    const resultadoDiv = document.getElementById('resultadoAtualizar');

    if (!id || !data || !hora || !medico || !paciente) {
        mostrarMensagem(resultadoDiv, '❌ Preencha todos os campos.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
        const consultaAtualizada = { data_consulta: data, hora_consulta: hora, id_medico: medico, id_paciente: paciente };

        const resposta = await fetch(`${API_BASE}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(consultaAtualizada)
        });

        if (!resposta.ok) throw new Error('Erro ao atualizar consulta.');

        mostrarMensagem(resultadoDiv, `✅ Consulta ${id} atualizada com sucesso!`, 'sucesso');

        await listarConsultas();

        setTimeout(limparFormAtualizar, 3000);
    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ ${erro.message}`, 'erro');
    }
}

// ✏️ PREENCHER FORMULÁRIO DE ATUALIZAÇÃO
async function preencherAtualizarConsulta(id) {
    mostrarAtualizar();

    // Definir elementos do DOM para os campos de atualização aqui
    const idInput = document.getElementById('idConsultaAtualizar');
    const resultadoDiv = document.getElementById('resultadoAtualizar');
    
    idInput.value = id;
    mostrarLoading(resultadoDiv);

    try {
        const resposta = await fetch(`${API_BASE}/${id}`);
        if (!resposta.ok) throw new Error('Consulta não encontrada para edição');
        const consulta = await resposta.json();
        
        // Preenche os campos (após garantir que os selects estão carregados)
		console.log("LINHA 317");
        await carregarSelects(); 
        
        document.getElementById('selectMedicoAtualizar').value = consulta.id_medico;
        document.getElementById('selectPacienteAtualizar').value = consulta.id_paciente;
        
        // Formato da data pode ser "YYYY-MM-DD" para o input type="date"
        document.getElementById('dataConsultaAtualizar').value = consulta.data_consulta.split('T')[0];
        
        // Preencher o horário atual
        document.getElementById('selectHorarioAtualizar').value = consulta.hora_consulta;

        resultadoDiv.innerHTML = ''; 
    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro ao carregar dados da consulta ${id}: ${erro.message}`, 'erro');
    }
}

// 🗑️ DELETAR CONSULTA POR ID (DA TABELA)
async function deletarConsultaPorId(id) {
    if (!confirm(`⚠️ Deseja realmente excluir a consulta ${id}?`)) return;

    try {
        const resposta = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (!resposta.ok) throw new Error('Erro ao deletar consulta.');

        await listarConsultas(); 
        console.log(`✅ Consulta ${id} deletada com sucesso!`);
    } catch (erro) {
        console.error('Erro ao deletar consulta:', erro);
    }
}

// 🔍 FILTRAR CONSULTAS POR STATUS (Trata a interação com a UI)
function filtrarConsultasPorStatus() {
    const selectStatus = document.getElementById('selectFiltroStatus');
    if (!selectStatus) return;

    // Chama a função centralizada de listagem
    listarConsultas(selectStatus.value);
}

// 🔍 LISTAR CONSULTAS (Função centralizada para listar e filtrar por Status)
async function listarConsultas(statusFiltro) {
    const resultadoDiv = document.getElementById('tabelaConsultas');
    mostrarLoading(resultadoDiv);

    let endpoint = API_BASE;

    // Aplica o filtro de status se for fornecido (ex: AGENDADA)
    if (statusFiltro && ['AGENDADA', 'CANCELADA', 'CONCLUIDA'].includes(statusFiltro.toUpperCase())) {
        endpoint = `${API_BASE}/status?status=${statusFiltro.toUpperCase()}`;
    }

    try {
        const resposta = await fetch(endpoint);
        if (!resposta.ok) throw new Error('Erro ao buscar consultas.');

        const consultas = await resposta.json();
		
		console.log(consultas)
        
        renderizarConsultas(consultas);

    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro ao listar consultas: ${erro.message}`, 'erro');
    }
}

function capitalize(str) {
  if (!str) return "";
  return str.charAt(0).toUpperCase() + str.slice(1).toLowerCase();
}


function renderizarConsultas(consultas) {
    const tbody = document.getElementById('tabelaConsultas');
    if (!tbody) return;
    tbody.innerHTML = '';
    
    if (consultas.length === 0) {
        exibirMensagemNaTabela('⚠️ Nenhuma consulta encontrada.', 4);
        return;
    }

    consultas.forEach(c => { 
        const row = tbody.insertRow();
		const actions = row.insertCell();
		actions.innerHTML = `
		<div class="action-icons">
		  <img src="img/edit-icon.png" alt="Editar" class="icon-edit-img" onclick="preencherAtualizarConsulta(${c.id})"> 
		  <img src="img/delete-icon.png" alt="Excluir" class="icon-delete-img" onclick="deletarConsultaPorId(${c.id})"> 
		</div>
		`;
        row.insertCell().textContent = c.id;
        row.insertCell().textContent = c.dataConsulta;
        row.insertCell().textContent = c.horaConsulta;
		row.insertCell().textContent = c.nomeMedico;
		row.insertCell().textContent = c.nomePaciente;
		row.insertCell().textContent = capitalize(c.status);
    });
}
