package br.com.fiap.bank.core.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;

/**
 * Contrato de entrada para {@code POST /api/v1/contas}.
 * <p>
 * {@code tipoConta} é recebido como String (não como o enum de domínio
 * {@code TipoConta}) propositalmente: isso evita que o tipo de domínio
 * precise ser referenciado por quem constrói este DTO, mantendo o
 * isolamento físico até o nível de import. A conversão e validação do
 * valor acontecem dentro do serviço de aplicação.
 * <p>
 * {@code agencia} é opcional no payload - quando ausente, o serviço de
 * aplicação assume o default "0001"; quando informada, deve ter 4 dígitos.
 */
public record CriarContaRequest(

        @NotBlank(message = "CPF do cliente é obrigatório.")
        String cpfCliente,

        @Pattern(regexp = "^\\d{4}$", message = "Agência deve conter exatamente 4 dígitos numéricos.")
        String agencia,

        @NotBlank(message = "Tipo de conta é obrigatório.")
        @Pattern(regexp = "(?i)^(CORRENTE|POUPANCA|SALARIO)$",
                message = "Tipo de conta deve ser CORRENTE, POUPANCA ou SALARIO.")
        String tipoConta,

        @NotNull(message = "Saldo inicial é obrigatório.")
        @DecimalMin(value = "0.00", message = "Saldo inicial não pode ser negativo.")
        BigDecimal saldoInicial) {

    private static final String AGENCIA_DEFAULT = "0001";

    /** Resolve a agência efetiva, aplicando o default quando o payload não informar uma. */
    public String agenciaOuDefault() {
        return (agencia == null || agencia.isBlank()) ? AGENCIA_DEFAULT : agencia;
    }
}
