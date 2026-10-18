package br.com.fiap.bank.plataforma.domain.gateway;

import br.com.fiap.bank.plataforma.domain.model.ContaCore;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import java.util.Optional;

/** Porta para o Core Bancário. */
public interface CoreBancarioGateway {

    /**
     * @return a conta do cliente, ou vazio se o Core respondeu que ela não existe
     * @throws br.com.fiap.bank.plataforma.domain.exception.ServicoExternoIndisponivelException
     *         se o Core estiver fora do ar ou responder com erro
     */
    Optional<ContaCore> consultarContaPorCpf(Cpf cpf);
}
