package br.com.fiap.bank.plataforma.infrastructure.persistence.adapter;

import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.repository.SolicitacaoCreditoRepository;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import br.com.fiap.bank.plataforma.infrastructure.persistence.jpa.SolicitacaoCreditoJpaRepository;
import br.com.fiap.bank.plataforma.infrastructure.persistence.mapper.SolicitacaoCreditoPersistenceMapper;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Repository Adapter: implementa a porta do domínio usando Spring Data JPA. */
@Component
@RequiredArgsConstructor
public class SolicitacaoCreditoRepositoryAdapter implements SolicitacaoCreditoRepository {

    private final SolicitacaoCreditoJpaRepository jpa;

    @Override
    public SolicitacaoCredito salvar(SolicitacaoCredito solicitacao) {
        var salva = jpa.save(SolicitacaoCreditoPersistenceMapper.toEntity(solicitacao));
        return SolicitacaoCreditoPersistenceMapper.toDomain(salva);
    }

    @Override
    public Optional<SolicitacaoCredito> buscarPorId(Long id) {
        return jpa.findById(id).map(SolicitacaoCreditoPersistenceMapper::toDomain);
    }

    @Override
    public List<SolicitacaoCredito> listarTodas() {
        return jpa.findAllByOrderByDataSolicitacaoDesc().stream()
                .map(SolicitacaoCreditoPersistenceMapper::toDomain)
                .toList();
    }

    @Override
    public List<SolicitacaoCredito> listarPorCpf(Cpf cpf) {
        return jpa.findByCpfClienteOrderByDataSolicitacaoDesc(cpf.valor()).stream()
                .map(SolicitacaoCreditoPersistenceMapper::toDomain)
                .toList();
    }
}
