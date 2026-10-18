package br.com.fiap.bank.core.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * Contrato de entrada para {@code PUT /api/v1/contas/{numeroConta}/saldo}.
 * {@code operacao} aceita apenas DEPOSITO ou SAQUE (case-insensitive).
 */
public record AtualizarSaldoRequest(

        @NotBlank(message = "Operação é obrigatória.")
        @Pattern(regexp = "(?i)^(DEPOSITO|SAQUE)$", message = "Operação deve ser DEPOSITO ou SAQUE.")
        String operacao,

        @NotNull(message = "Valor é obrigatório.")
        @DecimalMin(value = "0.00", inclusive = false, message = "Valor deve ser maior que zero.")
        BigDecimal valor) {

    public boolean isDeposito() {
        return "DEPOSITO".equalsIgnoreCase(operacao);
    }

    public boolean isSaque() {
        return "SAQUE".equalsIgnoreCase(operacao);
    }
}
