package br.com.fiap.bank.core.infrastructure.persistence.mapper;

import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import br.com.fiap.bank.core.infrastructure.persistence.entity.ClienteJpaEntity;

/** Converte entre a entidade de domínio {@link Cliente} e seu mapeamento JPA. */
public final class ClienteEntityMapper {

    private ClienteEntityMapper() {
    }

    public static ClienteJpaEntity toJpaEntity(Cliente cliente) {
        return new ClienteJpaEntity(
                cliente.getId(),
                cliente.getCpf().valor(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataNascimento(),
                cliente.getRendaMensal().valor(),
                cliente.getDataCadastro());
    }

    public static Cliente toDomain(ClienteJpaEntity entity) {
        return Cliente.reconstruir(
                entity.getId(),
                Cpf.of(entity.getCpf()),
                entity.getNome(),
                entity.getEmail(),
                entity.getDataNascimento(),
                Dinheiro.of(entity.getRendaMensal()),
                entity.getDataCadastro());
    }
}
