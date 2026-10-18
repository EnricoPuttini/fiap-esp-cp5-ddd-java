package br.com.fiap.bank.plataforma.application.mapper;

import br.com.fiap.bank.plataforma.application.dto.ContaClienteResponse;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponse;
import br.com.fiap.bank.plataforma.domain.model.ContaCore;
import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;

public final class SolicitacaoCreditoMapper {

    private SolicitacaoCreditoMapper() {
    }

    public static SolicitacaoCreditoResponse toResponse(SolicitacaoCredito s) {
        return new SolicitacaoCreditoResponse(
                s.getId(),
                s.getCpfCliente().valor(),
                s.getNomeCliente(),
                s.getTipoCredito(),
                s.getValorSolicitado(),
                s.getQuantidadeParcelas(),
                s.getRendaMensal(),
                s.getScoreCredito(),
                s.getStatus().name(),
                s.getParecerOperador(),
                s.getScrRestricoes(),
                s.getScoreBureau(),
                s.getDataSolicitacao(),
                s.getDataAtualizacao());
    }

    public static ContaClienteResponse toResponse(ContaCore c) {
        return new ContaClienteResponse(c.numeroConta(), c.agencia(), c.saldo(), c.status());
    }
}
