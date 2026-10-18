package br.com.fiap.bank.plataforma.domain.repository;

import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import java.util.List;
import java.util.Optional;

/** Porta de saída de persistência. Implementada na infrastructure (Ports &amp; Adapters). */
public interface SolicitacaoCreditoRepository {

    SolicitacaoCredito salvar(SolicitacaoCredito solicitacao);

    Optional<SolicitacaoCredito> buscarPorId(Long id);

    /** Da mais recente para a mais antiga. */
    List<SolicitacaoCredito> listarTodas();

    /** Da mais recente para a mais antiga. */
    List<SolicitacaoCredito> listarPorCpf(Cpf cpf);
}
