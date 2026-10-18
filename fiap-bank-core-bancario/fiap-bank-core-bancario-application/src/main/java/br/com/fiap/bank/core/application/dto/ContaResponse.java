package br.com.fiap.bank.core.application.dto;

import br.com.fiap.bank.core.domain.enums.StatusConta;
import br.com.fiap.bank.core.domain.enums.TipoConta;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/** Representação de saída de uma conta bancária. */
public record ContaResponse(
        UUID id,
        String numeroConta,
        String agencia,
        TipoConta tipoConta,
        String cpfCliente,
        BigDecimal saldo,
        StatusConta status,
        LocalDateTime dataAbertura) {
}
