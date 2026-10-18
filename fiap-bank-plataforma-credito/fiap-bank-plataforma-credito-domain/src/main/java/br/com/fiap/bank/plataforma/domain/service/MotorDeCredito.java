package br.com.fiap.bank.plataforma.domain.service;

import br.com.fiap.bank.plataforma.domain.model.DecisaoCredito;
import br.com.fiap.bank.plataforma.domain.model.StatusSolicitacao;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Domain Service com as regras automáticas de concessão (mesmas do código
 * legado): 1) restrição no SCR reprova; 2) score do Bureau &lt; 500 reprova;
 * 3) parcela acima de 30% da renda vai para a Mesa; 4) caso contrário aprova.
 */
public class MotorDeCredito {

    private static final int SCORE_MINIMO = 500;
    private static final BigDecimal COMPROMETIMENTO_MAXIMO = new BigDecimal("0.30");

    public DecisaoCredito analisar(boolean restricaoScr, int scoreBureau,
                                   BigDecimal valorSolicitado, int parcelas, BigDecimal rendaMensal) {
        if (parcelas <= 0) {
            throw new IllegalArgumentException("Quantidade de parcelas deve ser maior que zero.");
        }
        if (restricaoScr) {
            return new DecisaoCredito(StatusSolicitacao.REPROVADO,
                    "Solicitacao reprovada automaticamente devido a restricoes ativas no SCR (Banco Central).");
        }
        if (scoreBureau < SCORE_MINIMO) {
            return new DecisaoCredito(StatusSolicitacao.REPROVADO,
                    "Solicitacao reprovada automaticamente. Score no Bureau de Credito insuficiente ("
                            + scoreBureau + ").");
        }
        BigDecimal parcela = valorSolicitado.divide(BigDecimal.valueOf(parcelas), 2, RoundingMode.HALF_UP);
        BigDecimal limite = rendaMensal.multiply(COMPROMETIMENTO_MAXIMO);
        if (parcela.compareTo(limite) > 0) {
            return new DecisaoCredito(StatusSolicitacao.EM_ANALISE,
                    "Encaminhado para a Mesa de Operacoes de Credito. Parcela estimada (R$ " + parcela
                            + ") compromete mais de 30% da renda informada.");
        }
        return new DecisaoCredito(StatusSolicitacao.APROVADO,
                "Solicitacao aprovada automaticamente pelo Motor de Credito FIAP Bank!");
    }
}
