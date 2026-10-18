package br.com.fiap.bank.plataforma.infrastructure.persistence.mapper;

import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import br.com.fiap.bank.plataforma.infrastructure.persistence.entity.SolicitacaoCreditoJpaEntity;

/** Converte entre o modelo de domínio e o modelo relacional. */
public final class SolicitacaoCreditoPersistenceMapper {

    private SolicitacaoCreditoPersistenceMapper() {
    }

    public static SolicitacaoCreditoJpaEntity toEntity(SolicitacaoCredito d) {
        SolicitacaoCreditoJpaEntity e = new SolicitacaoCreditoJpaEntity();
        e.setId(d.getId());
        e.setCpfCliente(d.getCpfCliente().valor());
        e.setNomeCliente(d.getNomeCliente());
        e.setTipoCredito(d.getTipoCredito());
        e.setValorSolicitado(d.getValorSolicitado());
        e.setQuantidadeParcelas(d.getQuantidadeParcelas());
        e.setRendaMensal(d.getRendaMensal());
        e.setScoreCredito(d.getScoreCredito());
        e.setStatus(d.getStatus());
        e.setParecerOperador(d.getParecerOperador());
        e.setScrRestricoes(d.getScrRestricoes());
        e.setScoreBureau(d.getScoreBureau());
        e.setDataSolicitacao(d.getDataSolicitacao());
        e.setDataAtualizacao(d.getDataAtualizacao());
        return e;
    }

    public static SolicitacaoCredito toDomain(SolicitacaoCreditoJpaEntity e) {
        return SolicitacaoCredito.builder()
                .id(e.getId())
                .cpfCliente(Cpf.of(e.getCpfCliente()))
                .nomeCliente(e.getNomeCliente())
                .tipoCredito(e.getTipoCredito())
                .valorSolicitado(e.getValorSolicitado())
                .quantidadeParcelas(e.getQuantidadeParcelas())
                .rendaMensal(e.getRendaMensal())
                .scoreCredito(e.getScoreCredito())
                .status(e.getStatus())
                .parecerOperador(e.getParecerOperador())
                .scrRestricoes(e.getScrRestricoes())
                .scoreBureau(e.getScoreBureau())
                .dataSolicitacao(e.getDataSolicitacao())
                .dataAtualizacao(e.getDataAtualizacao())
                .build();
    }
}
