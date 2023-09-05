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
