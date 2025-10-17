// 📡 Configuração da API
const API_BASE = 'http://localhost:8080/medicos';

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
	console.log("VOLTAR MENU");
    Object.values(formularios).forEach(form => {
        form.classList.remove('ativo');
    });
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
	console.log("MOSTRAR ATUALIZAR")
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
    document.getElementById('nomeMedico').value = '';
    document.getElementById('especialidade').value = '';
    document.getElementById('resultadoAdicionar').innerHTML = '';
}

function limparFormAtualizar() {
    document.getElementById('idAtualizar').value = '';
    document.getElementById('nomeAtualizar').value = '';
    document.getElementById('especialidadeAtualizar').value = '';
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

// Função auxiliar para exibir feedback na tabela
function exibirMensagemNaTabela(mensagem, colspan = 4) { 
    const tbody = document.getElementById('tabelaMedicos');
    if (tbody) {
        tbody.innerHTML = `
            <tr><td colspan="${colspan}" style="text-align:center;">${mensagem}</td></tr>
        `;
    }
}

// 🔍 BUSCAR MÉDICO POR ID
async function buscarMedico() {
    const id = document.getElementById('idBusca').value;
    const resultadoDiv = document.getElementById('resultadoBusca');

    if (!id) {
        mostrarMensagem(resultadoDiv, '❌ Por favor, digite um ID válido.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
        const resposta = await fetch(`${API_BASE}/${id}`);
        
        if (!resposta.ok) {
            throw new Error('Médico não encontrado');
        }

        const medico = await resposta.json();
        
        resultadoDiv.innerHTML = `
            <div class="resultado">
                <h3>✅ Médico Encontrado</h3>
                <div class="dados-medico">
                    <div class="campo-dado"><strong>ID:</strong> ${medico.id}</div>
                    <div class="campo-dado"><strong>Nome:</strong> ${medico.nome}</div>
                    <div class="campo-dado"><strong>Especialidade:</strong> ${medico.especialidade}</div>
                </div>
            </div>
        `;

    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro: ${erro.message}`, 'erro');
    }
}

// ➕ ADICIONAR NOVO MÉDICO
async function adicionarMedico(event) {
	
	event.preventDefault();
	
	const nomeMedico = document.getElementById('nomeMedico').value;
	const especialidade = document.getElementById('especialidade').value;
	const resultadoDiv = document.getElementById('resultadoAdicionar');
	
    if (!nomeMedico || !especialidade) {
        mostrarMensagem(resultadoDiv, '❌ Por favor, preencha todos os campos.', 'erro');
        return;
    }

    mostrarLoading(resultadoDiv);

    try {
        const medicoDTO = { nomeMedico,especialidade };
		
		console.log(JSON.stringify(medicoDTO));

        const resposta = await fetch(`${API_BASE}/adicionarMedico`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(medicoDTO)
        });

        if (!resposta.ok) throw new Error('Erro ao cadastrar médico');

        const medicoSalvo = await resposta.json();
        
        mostrarMensagem(resultadoDiv, 
            `✅ Médico "${medicoSalvo.nomeMedico}" cadastrado com sucesso! ID: ${medicoSalvo.id}`, 
            'sucesso'
        );

        await listarMedicos();

        setTimeout(() => {
            limparFormAdicionar();
        }, 3000);

    } catch (erro) {
        mostrarMensagem(resultadoDiv, `❌ Erro ao cadastrar médico: ${erro.message}`, 'erro');
    }
}

// ✏️ ATUALIZAR MÉDICO
async function atualizarMedico(id) {
	
	const resultadoDiv = document.getElementById('resultadoAdicionar');
	const nomeMedico = document.getElementById('novoNome').value;
	const especialidade = document.getElementById('novaEspec').value;

    try {
        const medicoAtualizado = { nomeMedico, especialidade };

        const resposta = await fetch(`${API_BASE}/${id}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(medicoAtualizado)
        });

        if (!resposta.ok) throw new Error('Erro ao atualizar médico');

		const medicoSalvo = await resposta.json();
		       
		       mostrarMensagem(resultadoDiv, 
		           `✅ Médico "${medicoSalvo.nomeMedico}" cadastrado com sucesso! ID: ${medicoSalvo.id}`, 
		           'sucesso'
		       );

		       await listarMedicos();

		       setTimeout(() => {
		           limparFormAdicionar();
		       }, 3000);

		   } catch (erro) {
		       mostrarMensagem(resultadoDiv, `❌ Erro ao cadastrar médico: ${erro.message}`, 'erro');
		   }
}

async function preencherAtualizarMedico(id) {

    const resultadoDiv = document.getElementById('resultadoAdicionar');
	const tabela = document.getElementById('tabelaMedicos');
    
    if (!tabela) return;
	const linhas = tabela.querySelectorAll('tr');
	const targetLine = Array.from(linhas).find(linha => linha.cells[1].textContent === id.toString());
	const targetCells = targetLine.cells;
	for(i = 2; i < targetCells.length; i++) {
		const input = document.createElement('input');
		input.type = 'text';
		if (i == 2)
			input.id = 'novoNome';
		if( i == 3)
			input.id = 'novaEspec';
		input.classList.add('styled-input-table');
		input.placeholder = targetCells[i].textContent;
		targetCells[i].textContent = '';
		targetCells[i].appendChild(input);
	}
	

	targetCells[0].innerHTML = `
	<div class="action-icons">
		    <div class="btn-edit">
		      <button class="styled-button1" onclick="atualizarMedico(${id})">Atualizar</button>
		      <button class="styled-button2" onclick="listarMedicos()">Cancelar</button>
		    </div>
		  </div>
	`;
}

// 🗑️ DELETAR MÉDICO
async function deletarMedicoPorId(id) {
    if (!confirm(`⚠️ Tem certeza que deseja deletar o médico com ID ${id}? Esta ação não pode ser desfeita.`)) return;

    try {
        const resposta = await fetch(`${API_BASE}/${id}`, { method: 'DELETE' });
        if (!resposta.ok) throw new Error('Erro ao deletar médico');
        // Recarrega a lista para remover a linha deletada
        await listarMedicos(); 
    } catch (erro) {
        console.error('Erro ao deletar médico da lista:', erro);
        // Opcional: Adicionar feedback visual de erro na tabela
    }
}

// CARREGAR MÉDICOS
let allMedicos = [];

async function listarMedicos() {
    exibirMensagemNaTabela('⏳ Carregando médicos...');
    try {
        const resposta = await fetch(`${API_BASE}/listarMedicos`);
        if (!resposta.ok) throw new Error('Erro ao carregar médicos');
        allMedicos = await resposta.json();
        renderizarMedicos(allMedicos);
    } catch (erro) {
        console.error(erro);
        exibirMensagemNaTabela('❌ Erro ao carregar médicos.');
    }
}

function renderizarMedicos(medicos) {
    const tbody = document.getElementById('tabelaMedicos');
    if (!tbody) return;
    tbody.innerHTML = '';
    
    if (medicos.length === 0) {
        exibirMensagemNaTabela('⚠️ Nenhum médico encontrado.', 4);
        return;
    }

    medicos.forEach(m => { 
        const row = tbody.insertRow();
		const actions = row.insertCell();
		actions.innerHTML = `
		<div class="action-icons">
		  <img src="img/edit-icon.png" alt="Editar" class="icon-edit-img" onclick="preencherAtualizarMedico(${m.id})"> 
		  <img src="img/delete-icon.png" alt="Excluir" class="icon-delete-img" onclick="deletarMedicoPorId(${m.id})"> 
		</div>
		`;
        row.insertCell().textContent = m.id;
        row.insertCell().textContent = m.nomeMedico;
        row.insertCell().textContent = m.especialidade;
    });
}

// 🔍 FILTRAR MÉDICOS POR ID, NOME OU ESPECIALIDADE (Apenas a versão que usa a API)
async function filtrarMedicos() {
  const tipoFiltro = document.getElementById('tipoFiltro').value;
  const valorFiltro = document.getElementById('filtro').value.toLowerCase().trim();
  
  if (!valorFiltro) {
    await listarMedicos(); 
    return;
  }
  
  exibirMensagemNaTabela('🔍 Buscando médicos...');

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
      endpoint = `${API_BASE}/buscarMedico/${valorFiltro}`;
      break;
    case 'especialidade':
      endpoint = `${API_BASE}/buscarEspecialidade/${valorFiltro}`;
      break;
    default:
      console.error('Tipo de filtro inválido');
      return;
  }

  try {
    const resposta = await fetch(endpoint);
    if (!resposta.ok) throw new Error('Erro ao buscar médicos');

    let medicos = await resposta.json();

    if (!Array.isArray(medicos)) medicos = medicos ? [medicos] : [];

    renderizarMedicos(medicos); 

  } catch (erro) {
    console.error('Erro ao filtrar médicos:', erro);
    exibirMensagemNaTabela('❌ Erro ao buscar médicos.');
  }
}