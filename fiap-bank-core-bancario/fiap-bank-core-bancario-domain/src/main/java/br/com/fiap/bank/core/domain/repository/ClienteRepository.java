package br.com.fiap.bank.core.domain.repository;

import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.vo.Cpf;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Porta (interface) de persistência de {@link Cliente}, definida pelo
 * domínio e implementada pela infraestrutura (padrão Ports &amp; Adapters).
 * Todas as buscas retornam {@link Optional}, nunca {@code null}.
 */
public interface ClienteRepository {

    List<Cliente> findAll();

    Optional<Cliente> findById(UUID id);

    Optional<Cliente> findByCpf(Cpf cpf);

    boolean existsByCpf(Cpf cpf);

    Cliente save(Cliente cliente);

    void deleteById(UUID id);
}
