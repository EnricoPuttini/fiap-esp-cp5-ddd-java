package br.com.fiap.bank.plataforma.domain.gateway;

import br.com.fiap.bank.plataforma.domain.vo.Cpf;

/** Porta para o Bureau de Crédito (Serasa/SPC): score de risco de 0 a 1000. */
public interface BureauGateway {
    int consultarScore(Cpf cpf);
}
