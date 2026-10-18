package br.com.fiap.bank.core.infrastructure.persistence.adapter;

import br.com.fiap.bank.core.domain.model.Conta;
import br.com.fiap.bank.core.domain.repository.ContaRepository;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.infrastructure.persistence.jpa.ContaJpaRepository;
import br.com.fiap.bank.core.infrastructure.persistence.mapper.ContaEntityMapper;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

/**
 * Adapter que implementa a porta {@link ContaRepository} do domínio
 * usando Spring Data JPA.
 */
@Repository
public class ContaRepositoryAdapter implements ContaRepository {

    private final ContaJpaRepository jpaRepository;

    public ContaRepositoryAdapter(ContaJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public List<Conta> findAll() {
        return jpaRepository.findAll().stream()
                .map(ContaEntityMapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Conta> findById(UUID id) {
        return jpaRepository.findById(id).map(ContaEntityMapper::toDomain);
    }

    @Override
    public Optional<Conta> findByNumeroConta(String numeroConta) {
        return jpaRepository.findByNumeroConta(numeroConta).map(ContaEntityMapper::toDomain);
    }

    @Override
    public Optional<Conta> findByCpfCliente(Cpf cpf) {
        return jpaRepository.findByCpfCliente(cpf.valor()).map(ContaEntityMapper::toDomain);
    }

    @Override
    public boolean existsByNumeroConta(String numeroConta) {
        return jpaRepository.existsByNumeroConta(numeroConta);
    }

    @Override
    public Conta save(Conta conta) {
        var salva = jpaRepository.save(ContaEntityMapper.toJpaEntity(conta));
        return ContaEntityMapper.toDomain(salva);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.deleteById(id);
    }
}
