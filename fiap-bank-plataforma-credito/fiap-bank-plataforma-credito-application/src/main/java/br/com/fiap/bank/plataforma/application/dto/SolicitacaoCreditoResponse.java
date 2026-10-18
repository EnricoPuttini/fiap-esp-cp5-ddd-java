package br.com.fiap.bank.plataforma.application.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representação de saída da solicitação. Os nomes dos campos são idênticos aos
 * do contrato legado, o que mantém Angular e Mesa funcionando sem alteração.
 */
public record SolicitacaoCreditoResponse(
        Long id,
        String cpfCliente,
        String nomeCliente,
        String tipoCredito,
        BigDecimal valorSolicitado,
        Integer quantidadeParcelas,
        BigDecimal rendaMensal,
        Integer scoreCredito,
        String status,
        String parecerOperador,
        Boolean scrRestricoes,
        Integer scoreBureau,
        LocalDateTime dataSolicitacao,
        LocalDateTime dataAtualizacao) {
}
