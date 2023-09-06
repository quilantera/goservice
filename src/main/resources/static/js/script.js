const agendamentosPendentes = document.getElementById('agendamentosPendentesBadge');
document.addEventListener("DOMContentLoaded", function atualizarBadge() {
   axios.get('/cliente/agendamentos-pedentes')
        .then(function(response) {
            agendamentosPendentes.textContent = response.data;
        })
        .catch(function(error) {
            console.error(error);
        });
});

document.addEventListener("DOMContentLoaded", function bloquearDatas() {
    var dataInput = document.getElementById("data");
    var dataAtual = new Date().toISOString().split("T")[0];
    dataInput.min = dataAtual;
});

(() => {
  'use strict'

  const forms = document.querySelectorAll('.needs-validation')

  Array.from(forms).forEach(form => {
    form.addEventListener('submit', event => {
      if (!form.checkValidity()) {
        event.preventDefault()
        event.stopPropagation()
      }

      form.classList.add('was-validated')
    }, false)
  })
})()