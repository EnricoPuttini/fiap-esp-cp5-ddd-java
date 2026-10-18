package br.com.fiap.bank.core.domain.repository;

import br.com.fiap.bank.core.domain.model.Conta;
import br.com.fiap.bank.core.domain.vo.Cpf;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta (interface) de persistência de {@link Conta}, definida pelo
 * domínio e implementada pela infraestrutura (padrão Ports &amp; Adapters).
 */
public interface ContaRepository {

    List<Conta> findAll();

    Optional<Conta> findById(UUID id);

    Optional<Conta> findByNumeroConta(String numeroConta);

    Optional<Conta> findByCpfCliente(Cpf cpf);

    boolean existsByNumeroConta(String numeroConta);

    Conta save(Conta conta);

    void deleteById(UUID id);
}
