package br.com.fiap.bank.core.infrastructure.persistence.mapper;

import br.com.fiap.bank.core.domain.enums.StatusConta;
import br.com.fiap.bank.core.domain.enums.TipoConta;
import br.com.fiap.bank.core.domain.model.Conta;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import br.com.fiap.bank.core.infrastructure.persistence.entity.ContaJpaEntity;

/** Converte entre a entidade de domínio {@link Conta} e seu mapeamento JPA. */
public final class ContaEntityMapper {

    private ContaEntityMapper() {
    }

    public static ContaJpaEntity toJpaEntity(Conta conta) {
        return new ContaJpaEntity(
                conta.getId(),
                conta.getNumeroConta(),
                conta.getAgencia(),
                conta.getTipoConta().name(),
                conta.getCpfCliente().valor(),
                conta.getSaldo().valor(),
                conta.getStatus().name(),
                conta.getDataAbertura());
    }

    public static Conta toDomain(ContaJpaEntity entity) {
        return Conta.reconstruir(
                entity.getId(),
                entity.getNumeroConta(),
                entity.getAgencia(),
                TipoConta.valueOf(entity.getTipoConta()),
                Cpf.of(entity.getCpfCliente()),
                Dinheiro.of(entity.getSaldo()),
                StatusConta.valueOf(entity.getStatus()),
                entity.getDataAbertura());
    }
}
