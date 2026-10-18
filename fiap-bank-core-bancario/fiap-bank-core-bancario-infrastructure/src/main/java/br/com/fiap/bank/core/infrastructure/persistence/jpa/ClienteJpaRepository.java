package br.com.fiap.bank.core.infrastructure.persistence.jpa;

import br.com.fiap.bank.core.infrastructure.persistence.entity.ClienteJpaEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositório técnico Spring Data JPA - detalhe de infraestrutura, nunca exposto ao domínio. */
public interface ClienteJpaRepository extends JpaRepository<ClienteJpaEntity, UUID> {

    Optional<ClienteJpaEntity> findByCpf(String cpf);

    boolean existsByCpf(String cpf);
}
