package br.com.fiap.bank.core.application.mapper;

import br.com.fiap.bank.core.application.dto.ClienteResponse;
import br.com.fiap.bank.core.domain.model.Cliente;

/** Converte a entidade de domínio {@link Cliente} para o DTO de saída. */
public final class ClienteMapper {

    private ClienteMapper() {
    }

    public static ClienteResponse toResponse(Cliente cliente) {
        return new ClienteResponse(
                cliente.getId(),
                cliente.getCpf().valor(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataNascimento(),
                cliente.getRendaMensal().valor(),
                cliente.getDataCadastro());
    }
}
