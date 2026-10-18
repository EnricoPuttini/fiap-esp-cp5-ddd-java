package br.com.fiap.bank.plataforma.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * Contrato de entrada para abrir uma solicitação de crédito. O CPF pode vir com
 * ou sem máscara; a validação do formato é feita pelo Value Object de domínio.
 */
public record SolicitarCreditoRequest(

        @NotBlank(message = "CPF do cliente é obrigatório.")
        String cpfCliente,

        @NotBlank(message = "Nome do cliente é obrigatório.")
        @Size(max = 255, message = "Nome do cliente deve ter no máximo 255 caracteres.")
        String nomeCliente,

        @NotBlank(message = "Tipo de crédito é obrigatório.")
        String tipoCredito,

        @NotNull(message = "Valor solicitado é obrigatório.")
        @DecimalMin(value = "0.01", message = "Valor solicitado deve ser maior que zero.")
        BigDecimal valorSolicitado,

        @NotNull(message = "Quantidade de parcelas é obrigatória.")
        @Min(value = 1, message = "Quantidade de parcelas deve ser no mínimo 1.")
        Integer quantidadeParcelas,

        @NotNull(message = "Renda mensal é obrigatória.")
        @PositiveOrZero(message = "Renda mensal não pode ser negativa.")
        BigDecimal rendaMensal) {
}
