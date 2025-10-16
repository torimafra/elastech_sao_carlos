// 📡 Configuração da API
const API_BASE = 'http://localhost:8080';

/* ==========================
   LOGIN / LOGOUT
========================== */
function login() {
  window.location.href = 'home.html';
}

function logout() {
  window.location.href = 'index.html';
}

/* ==========================
   NAVEGAÇÃO HOME CONSULTAS
========================== */
document.addEventListener('DOMContentLoaded', () => {
  const ctaButton = document.querySelector('.cta-button');
  if (ctaButton) {
    ctaButton.addEventListener('click', () => {
      window.location.href = 'consultas.html';
    });
  }
});

/* ==========================
   DETECTAR PÁGINA ATUAL
========================== */
window.onload = async function () {
  const page = window.location.pathname.split('/').pop();

  switch (page) {
    case 'medicos.html':
      await loadMedicos();
      break;
    case 'pacientes.html':
      await loadPacientes();
      break;
    case 'consultas.html':
      await loadConsultas();
      await loadSelectsConsulta();
      break;
  }
};
