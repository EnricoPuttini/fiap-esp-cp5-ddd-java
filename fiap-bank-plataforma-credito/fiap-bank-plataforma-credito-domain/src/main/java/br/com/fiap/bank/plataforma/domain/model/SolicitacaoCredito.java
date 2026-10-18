package br.com.fiap.bank.plataforma.domain.model;

import br.com.fiap.bank.plataforma.domain.exception.SolicitacaoJaDecididaException;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Entidade comportamental (aggregate root) de uma solicitação de crédito.
 * As transições de estado ficam aqui dentro - não há setters públicos -, de
 * modo que nenhuma camada externa consegue colocar a solicitação num estado
 * inconsistente.
 */
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class SolicitacaoCredito {

    private final Long id;
    private final Cpf cpfCliente;
    private final String nomeCliente;
    private final String tipoCredito;
    private final BigDecimal valorSolicitado;
    private final Integer quantidadeParcelas;
    private final BigDecimal rendaMensal;
    private final Integer scoreCredito;
    private final Boolean scrRestricoes;
    private final Integer scoreBureau;
    private final LocalDateTime dataSolicitacao;
    private StatusSolicitacao status;
    private String parecerOperador;
    private LocalDateTime dataAtualizacao;

    /** Abre uma nova solicitação já com o resultado do motor de crédito aplicado. */
    public static SolicitacaoCredito abrir(Cpf cpf, String nome, String tipoCredito,
                                           BigDecimal valor, int parcelas, BigDecimal renda,
                                           boolean restricaoScr, int scoreBureau,
                                           DecisaoCredito decisao) {
        LocalDateTime agora = LocalDateTime.now();
        return SolicitacaoCredito.builder()
                .cpfCliente(cpf)
                .nomeCliente(nome)
                .tipoCredito(tipoCredito)
                .valorSolicitado(valor)
                .quantidadeParcelas(parcelas)
                .rendaMensal(renda)
                .scoreCredito(scoreBureau)
                .scrRestricoes(restricaoScr)
                .scoreBureau(scoreBureau)
                .status(decisao.status())
                .parecerOperador(decisao.parecer())
                .dataSolicitacao(agora)
                .dataAtualizacao(agora)
                .build();
    }

    public boolean estaEmAnalise() {
        return status == StatusSolicitacao.EM_ANALISE;
    }

    /** Aprovação manual feita pela Mesa de Operações. */
    public void aprovarPelaMesa(String parecer) {
        exigirEmAnalise();
        this.status = StatusSolicitacao.APROVADO;
        this.parecerOperador = "Aprovado pela Mesa de Operacoes: "
                + (parecer != null && !parecer.isBlank() ? parecer : "OK");
        this.dataAtualizacao = LocalDateTime.now();
    }

    /** Reprovação manual feita pela Mesa de Operações. */
    public void reprovarPelaMesa(String motivo) {
        exigirEmAnalise();
        this.status = StatusSolicitacao.REPROVADO;
        this.parecerOperador = "Reprovado pela Mesa de Operacoes. Motivo: "
                + (motivo != null && !motivo.isBlank() ? motivo : "Inconformidade nos criterios de risco");
        this.dataAtualizacao = LocalDateTime.now();
    }

    private void exigirEmAnalise() {
        if (!estaEmAnalise()) {
            throw new SolicitacaoJaDecididaException(
                    "A solicitacao #" + id + " nao esta em analise (status atual: " + status + ").");
        }
    }
}
