package br.com.fiap.bank.core.infrastructure.persistence.jpa;

import br.com.fiap.bank.core.infrastructure.persistence.entity.ContaJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositório técnico Spring Data JPA - detalhe de infraestrutura, nunca exposto ao domínio. */
public interface ContaJpaRepository extends JpaRepository<ContaJpaEntity, UUID> {

    Optional<ContaJpaEntity> findByNumeroConta(String numeroConta);

    Optional<ContaJpaEntity> findByCpfCliente(String cpfCliente);

    boolean existsByNumeroConta(String numeroConta);
}
