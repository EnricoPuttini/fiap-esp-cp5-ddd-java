package br.com.fiap.bank.plataforma.infrastructure.persistence.jpa;

import br.com.fiap.bank.plataforma.infrastructure.persistence.entity.SolicitacaoCreditoJpaEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SolicitacaoCreditoJpaRepository extends JpaRepository<SolicitacaoCreditoJpaEntity, Long> {

    List<SolicitacaoCreditoJpaEntity> findByCpfClienteOrderByDataSolicitacaoDesc(String cpfCliente);

    List<SolicitacaoCreditoJpaEntity> findAllByOrderByDataSolicitacaoDesc();
}
