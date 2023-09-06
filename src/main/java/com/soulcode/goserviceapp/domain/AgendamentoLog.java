package com.soulcode.goserviceapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.LocalDateTime;

@Document(collection = "agendamentos")
public class AgendamentoLog {


    @Id
    private String id;

    private Long idCliente;
    private String nomeCliente;
    private Long idPrestador;
    private String nomePrestador;
    private Long Servico;
    private String Agendamento;

    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime dataLog = LocalDateTime.now();

    public AgendamentoLog(String id, Long idCliente, String nomeCliente, Long idPrestador, String nomePrestador, Long servico, String agendamento, LocalDateTime dataLog) {
        this.id = id;
        this.idCliente = idCliente;
        this.nomeCliente = nomeCliente;
        this.idPrestador = idPrestador;
        this.nomePrestador = nomePrestador;
        Servico = servico;
        Agendamento = agendamento;
        this.dataLog = dataLog;
    }

    public AgendamentoLog() {
    }

    public String getId() {
        return id;
    }

    public Long getIdCliente() {
        return idCliente;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public Long getIdPrestador() {
        return idPrestador;
    }

    public String getNomePrestador() {
        return nomePrestador;
    }

    public Long getServico() {
        return Servico;
    }

    public String getAgendamento() {
        return Agendamento;
    }

    public LocalDateTime getDataLog() {
        return dataLog;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setIdCliente(Long idCliente) {
        this.idCliente = idCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public void setIdPrestador(Long idPrestador) {
        this.idPrestador = idPrestador;
    }

    public void setNomePrestador(String nomePrestador) {
        this.nomePrestador = nomePrestador;
    }

    public void setServico(Long servico) {
        Servico = servico;
    }

    public void setAgendamento(String agendamento) {
        Agendamento = agendamento;
    }

    public void setDataLog(LocalDateTime dataLog) {
        this.dataLog = dataLog;
    }
}