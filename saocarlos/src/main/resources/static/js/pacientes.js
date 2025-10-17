// 📡 Configuração da API
const API_BASE = 'http://localhost:8080/pacientes';

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
      break;
  }
};

// 🎯 Elementos do DOM
const formularios = {
    buscar: document.getElementById('formBuscar'),
    adicionar: document.getElementById('formAdicionar'),
    atualizar: document.getElementById('formAtualizar'),
    deletar: document.getElementById('formDeletar')
};

// 🏠 Funções de Navegação
function voltarMenu() {
    Object.values(formularios).forEach(form => form.classList.remove('ativo'));
    limparTodosResultados();
}

function mostrarBuscar() {
    voltarMenu();
    formularios.buscar.classList.add('ativo');
}

function mostrarAdicionar() {
    voltarMenu();
    formularios.adicionar.classList.add('ativo');
}

function mostrarAtualizar() {
    voltarMenu();
    formularios.atualizar.classList.add('ativo');
}

function mostrarDeletar() {
    voltarMenu();
    formularios.deletar.classList.add('ativo');
}

// 🧹 Funções de Limpeza
function limparTodosResultados() {
    document.getElementById('resultadoBusca').innerHTML = '';
    document.getElementById('resultadoAdicionar').innerHTML = '';
    document.getElementById('resultadoAtualizar').innerHTML = '';
    document.getElementById('resultadoDeletar').innerHTML = '';
}

function limparFormAdicionar() {
    document.getElementById('nomePaciente').value = '';
    document.getElementById('cpfPaciente').value = '';
    document.getElementById('resultadoAdicionar').innerHTML = '';
}

function limparFormAtualizar() {
    document.getElementById('idAtualizar').value = '';
    document.getElementById('nomeAtualizar').value = '';
    document.getElementById('cpfAtualizar').value = '';
    document.getElementById('resultadoAtualizar').innerHTML = '';
}

function limparFormDeletar() {
    document.getElementById('idDeletar').value = '';
    document.getElementById('resultadoDeletar').innerHTML = '';
}

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

function exibirMensagemNaTabela(mensagem, colspan = 4) { 
    const tbody = document.getElementById('tabelaPacientes');
    if (tbody) {
        tbody.innerHTML = `
            <tr><td colspan="${colspan}" style="text-align:center;">${mensagem}</td></tr>
        `;
    }
}

// 🔍 BUSCAR PACIENTE POR ID
async function buscarPaciente() {
    const id = document.getElementById('idBusca').value;
    const resultadoDiv = document.getElementById('resultadoBusca');

    if (!id) {
        mostrarMensagem(resultadoDiv, '❌ Por favor, digite um ID válido.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
        const resposta = await fetch(`${API_BASE}/${id}`);
        if (!resposta.ok) throw new Error('Paciente não encontrado');

        const paciente = await resposta.json();

        resultadoDiv.innerHTML = `
            <div class="resultado">
                <h3>✅ Paciente Encontrado</h3>
                <div class="dados-paciente">
                    <div class="campo-dado"><strong>ID:</strong> ${paciente.id}</div>
                    <div class="campo-dado"><strong>Nome:</strong> ${paciente.nome}</div>
                    <div class="campo-dado"><strong>CPF:</strong> ${paciente.cpf}</div>
                </div>
            </div>
        `;
    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro: ${erro.message}`, 'erro');
    }
}

// ➕ ADICIONAR PACIENTE
async function adicionarPaciente(evento) {
	
	event.preventDefault();
	
    const nome = document.getElementById('nomePaciente').value;
    const cpf = document.getElementById('cpfPaciente').value;
    const resultadoDiv = document.getElementById('resultadoAdicionar');

    if (!nome || !cpf) {
        mostrarMensagem(resultadoDiv, '❌ Por favor, preencha todos os campos.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
        const pacienteDTO = { nome, cpf };

        const resposta = await fetch(`${API_BASE}/adicionarPaciente`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(pacienteDTO)
        });

        if (!resposta.ok) throw new Error('Erro ao cadastrar paciente');

        const pacienteSalvo = await resposta.json();

        mostrarMensagem(resultadoDiv, 
            `✅ Paciente "${pacienteSalvo.nome}" cadastrado com sucesso! ID: ${pacienteSalvo.id}`, 
            'sucesso'
        );
        
        await listarPacientes();

        setTimeout(() => {
            limparFormAdicionar();
        }, 3000);

    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro ao cadastrar paciente: ${erro.message}`, 'erro');
    }
}

// ✏️ ATUALIZAR PACIENTE
async function atualizarPaciente(id) {

	const resultadoDiv = document.getElementById('resultadoAdicionar');
	const nome = document.getElementById('novoNomePaciente').value;
	const cpf = document.getElementById('novoCpf').value;

	try {
	    const pacienteAtualizado = { nome, cpf };

	    const resposta = await fetch(`${API_BASE}/${id}`, {
	        method: 'PUT',
	        headers: { 'Content-Type': 'application/json' },
	        body: JSON.stringify(pacienteAtualizado)
	    });

	    if (!resposta.ok) throw new Error('Erro ao atualizar paciente');

		const pacienteSalvo = await resposta.json();
		       
		       mostrarMensagem(resultadoDiv, 
		           `✅ Médico "${pacienteSalvo.nomePaciente}" cadastrado com sucesso! ID: ${pacienteSalvo.id}`, 
		           'sucesso'
		       );

		       await listarPacientes();

		       setTimeout(() => {
		           limparFormAdicionar();
		       }, 3000);

		   } catch (erro) {
		       mostrarMensagem(resultadoDiv, `❌ Erro ao cadastrar paciente: ${erro.message}`, 'erro');
		   }
}

// ✅ Função chamada pelo botão 'Editar' na tabela
async function preencherAtualizarPaciente(id) {

	const resultadoDiv = document.getElementById('resultadoAdicionar');
	const tabela = document.getElementById('tabelaPacientes');

	if (!tabela) return;
	const linhas = tabela.querySelectorAll('tr');
	const targetLine = Array.from(linhas).find(linha => linha.cells[1].textContent === id.toString());
	const targetCells = targetLine.cells;
	for(i = 2; i < targetCells.length; i++) {
		const input = document.createElement('input');
		input.type = 'text';
		if (i == 2)
			input.id = 'novoNomePaciente';
		if( i == 3)
			input.id = 'novoCpf';
		input.classList.add('styled-input-table');
		input.placeholder = targetCells[i].textContent;
		targetCells[i].textContent = '';
		targetCells[i].appendChild(input);
	}


	targetCells[0].innerHTML = `
	<div class="action-icons">
	    <div class="btn-edit">
	      <button class="styled-button1" onclick="atualizarPaciente(${id})">Atualizar</button>
	      <button class="styled-button2" onclick="listarPacientes()">Cancelar</button>
	    </div>
	  </div>
	`;
}

// 🗑️ DELETAR PACIENTE
async function deletarPacientePorId(id) {
    if (!confirm(`⚠️ Tem certeza que deseja deletar o paciente com ID ${id}? Esta ação não pode ser desfeita.`)) return;

    try {
        const resposta = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (!resposta.ok) throw new Error('Erro ao deletar paciente');

        // ✅ Importante: Recarrega a lista para remover a linha deletada
        await listarPacientes(); 
    } catch (erro) {
        console.error('Erro ao deletar paciente da lista:', erro);
        // Opcional: Adicionar feedback visual de erro na tabela
    }
}

// LOAD PACIENTES
let allPacientes = [];

async function listarPacientes() {
    exibirMensagemNaTabela('⏳ Carregando pacientes...'); 
    try {
        const resposta = await fetch(`${API_BASE}/listarPacientes`);
        if (!resposta.ok) throw new Error('Erro ao carregar pacientes');
        allPacientes = await resposta.json();
        
        if (allPacientes.length === 0) {
            exibirMensagemNaTabela('⚠️ Nenhum paciente cadastrado.', 4);
            return;
        }
        console.log(allPacientes);
        renderizarPacientes(allPacientes);
    } catch (erro) {
        console.error(erro);
        exibirMensagemNaTabela('❌ Erro ao carregar pacientes.');
    }
}

function renderizarPacientes(pacientes) {
    const tbody = document.getElementById('tabelaPacientes');
    if (!tbody) return;
    tbody.innerHTML = '';

    if (pacientes.length === 0) {
        exibirMensagemNaTabela('⚠️ Nenhum paciente encontrado com este filtro.', 4);
        return;
    }

    pacientes.forEach(p => {
        const row = tbody.insertRow();
		const actions = row.insertCell();
		actions.innerHTML = `
			<div class="action-icons">
		    <img src="img/edit-icon.png" alt="Editar" class="icon-edit-img" onclick="preencherAtualizarPaciente(${p.id})"></button>
		    <img src="img/delete-icon.png" alt="Excluir" class="icon-delete-img" onclick="deletarPacientePorId(${p.id})"></button>
			</div>
		`;
        row.insertCell().textContent = p.id;
        row.insertCell().textContent = p.nome;
        row.insertCell().textContent = p.cpf;
    });
}


// 🔍 FILTRAR PACIENTES POR ID, NOME OU CPF (REVISADO)
async function filtrarPacientes() {
	const tipoFiltro = document.getElementById('tipoFiltro').value; 
	const valorFiltro = document.getElementById('filtro').value.toLowerCase().trim();

    if (!valorFiltro) {
        await listarPacientes();
        return;
    }

    exibirMensagemNaTabela('🔍 Buscando pacientes...');

	let endpoint = '';
    switch (tipoFiltro) {
        case 'id':
            if (isNaN(valorFiltro) || valorFiltro === '') {
                 exibirMensagemNaTabela('⚠️ O ID deve ser um número válido.');
                 return;
            }
            endpoint = `${API_BASE}/${valorFiltro}`; 
            break;
        case 'nome':
            endpoint = `${API_BASE}/buscarPaciente/${valorFiltro}`;
            break;
        case 'cpf':
			endpoint = `${API_BASE}/buscarPacienteCpf/${valorFiltro}`;
           	break;
        default:
            console.error('Tipo de filtro inválido');
            return;
    }

    try {
        const resposta = await fetch(endpoint);
        if (!resposta.ok) throw new Error('Erro ao buscar pacientes');

        let pacientes = await resposta.json();
		
        if (!Array.isArray(pacientes)) pacientes = pacientes ? [pacientes] : [];

        renderizarPacientes(pacientes);

    } catch (erro) {
        console.error('Erro ao filtrar pacientes:', erro);
        exibirMensagemNaTabela('❌ Erro ao buscar pacientes.');
    }
}