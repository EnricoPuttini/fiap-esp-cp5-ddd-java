package br.com.fiap.bank.plataforma.domain.gateway;

import br.com.fiap.bank.plataforma.domain.vo.Cpf;

/** Porta para o CRM / sistema de notificações ao cliente (SMS/e-mail). */
public interface CrmGateway {
    void notificar(Cpf cpf, String mensagem);
}
