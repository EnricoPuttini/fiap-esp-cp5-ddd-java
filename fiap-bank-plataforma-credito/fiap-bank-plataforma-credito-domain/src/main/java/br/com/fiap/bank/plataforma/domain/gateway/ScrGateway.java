package br.com.fiap.bank.plataforma.domain.gateway;

import br.com.fiap.bank.plataforma.domain.vo.Cpf;

/** Porta para o Sistema de Informações de Crédito do Banco Central (SCR). */
public interface ScrGateway {
    boolean possuiRestricoes(Cpf cpf);
}
