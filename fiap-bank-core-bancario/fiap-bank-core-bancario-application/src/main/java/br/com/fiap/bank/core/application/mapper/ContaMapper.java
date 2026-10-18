package br.com.fiap.bank.core.application.mapper;

import br.com.fiap.bank.core.application.dto.ContaResponse;
import br.com.fiap.bank.core.domain.model.Conta;

/** Converte a entidade de domínio {@link Conta} para o DTO de saída. */
public final class ContaMapper {

    private ContaMapper() {
    }

    public static ContaResponse toResponse(Conta conta) {
        return new ContaResponse(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getAgencia(),
                conta.getTipoConta(),
                conta.getCpfCliente().valor(),
                conta.getSaldo().valor(),
                conta.getStatus(),
                conta.getDataAbertura());
    }
}
