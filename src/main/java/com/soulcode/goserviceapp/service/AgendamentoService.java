package com.soulcode.goserviceapp.service;

import com.soulcode.goserviceapp.domain.*;
import com.soulcode.goserviceapp.domain.enums.StatusAgendamento;
import com.soulcode.goserviceapp.repository.AgendamentoLogRepository;
import com.soulcode.goserviceapp.repository.AgendamentoRepository;
import com.soulcode.goserviceapp.service.exceptions.AgendamentoNaoEncontradoException;
import com.soulcode.goserviceapp.service.exceptions.ConflitoHorarioException;
import com.soulcode.goserviceapp.service.exceptions.StatusAgendamentoImutavelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {
    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private  ServicoService servicoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private PrestadorService prestadorService;

    @Autowired
    private AgendamentoLogRepository agendamentoLogRepository;

    public Agendamento findById(Long id){
        Optional<Agendamento> agendamento = agendamentoRepository.findById(id);
        if(agendamento.isPresent()) {
            return agendamento.get();
        }
        throw new AgendamentoNaoEncontradoException();
    }

    public Agendamento create(Authentication authentication, Long servicoId, Long prestadorId, LocalDate data, LocalTime hora) {
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Prestador prestador = prestadorService.findById(prestadorId);
        Servico servico = servicoService.findById(servicoId);
        AgendamentoLog agendamentolog = new AgendamentoLog();

        if (isHorarioDisponivel(prestador, data, hora)) {
            Agendamento agendamento = new Agendamento();
            agendamento.setCliente(cliente);
            agendamento.setPrestador(prestador);
            agendamento.setServico(servico);
            agendamento.setData(data);
            agendamento.setHora(hora);
            agendamentolog.setIdCliente(cliente.getId());
            agendamentolog.setNomeCliente(cliente.getNome());
            agendamentolog.setIdPrestador(prestador.getId());
            agendamentolog.setNomePrestador(prestador.getNome());
            agendamentolog.setServico(servico.getId());
            agendamentoLogRepository.save(agendamentolog);
            return agendamentoRepository.save(agendamento);
        } else {
            throw new ConflitoHorarioException("Indisponível: O prestador já possui um agendamento nesse horário.");
        }
    }

    public boolean isHorarioDisponivel(Prestador prestador, LocalDate data, LocalTime hora) {
        Long prestadorId = prestador.getId();
        List<Agendamento> agendamentos = agendamentoRepository.findByPrestadorAndData(prestadorId, data);

        for(Agendamento agendamento : agendamentos) {
            if(agendamento.getHora().equals(hora)) {
                return false;
            }
        }
        return true;
    }
    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByCliente(Authentication authentication){
        System.err.println("BUSCANDO AGENDAMENTOS CLIENTE NO BANCO...");
        Cliente cliente = clienteService.findAuthenticated(authentication);
        return agendamentoRepository.findByClienteEmail(cliente.getEmail());
    }

    @Cacheable(cacheNames = "redisCache")
    public List<Agendamento> findByPrestador(Authentication authentication){
        System.err.println("BUSCANDO AGENDAMENTOS PRESTADOR NO BANCO...");
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        return  agendamentoRepository.findByPrestadorEmail(prestador.getEmail());
    }

    public void cancelAgendaPrestador(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_PRESTADOR);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void confirmAgenda(Authentication authentication, Long id){
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if(agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONFIRMADO);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void cancelAgendaCliente(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.AGUARDANDO_CONFIRMACAO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CANCELADO_PELO_CLIENTE);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }

    public void completeAgenda(Authentication authentication, Long id){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        Agendamento agendamento = findById(id);
        if (agendamento.getStatusAgendamento().equals(StatusAgendamento.CONFIRMADO)){
            agendamento.setStatusAgendamento(StatusAgendamento.CONCLUIDO);
            agendamentoRepository.save(agendamento);
            return;
        }
        throw new StatusAgendamentoImutavelException();
    }
    public Long notificarAgendamentosPendentes(Authentication authentication){
        Cliente cliente = clienteService.findAuthenticated(authentication);
        return agendamentoRepository.notificarAgendamentosPendentes(cliente.getEmail());
    }

    public List<Agendamento> buscarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Authentication authentication) {
        Prestador prestador = prestadorService.findAuthenticated(authentication);
        if (dataInicio == null || dataFim == null) {
            throw new RuntimeException();
        }
        return agendamentoRepository.findByDataAgendamentoBetween(dataInicio, dataFim, prestador.getId());
    }

}
