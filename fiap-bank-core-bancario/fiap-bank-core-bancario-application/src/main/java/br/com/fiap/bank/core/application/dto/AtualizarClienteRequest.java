package br.com.fiap.bank.core.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Contrato de entrada para {@code PUT /api/v1/clientes/{id}}.
 * O CPF não é um campo editável aqui de propósito: é a chave de negócio
 * do cliente e não deve ser alterada por uma atualização cadastral.
 */
public record AtualizarClienteRequest(

        @NotBlank(message = "Nome é obrigatório.")
        @Size(min = 3, max = 150, message = "Nome deve ter entre 3 e 150 caracteres.")
        String nome,

        @NotBlank(message = "E-mail é obrigatório.")
        @Email(message = "E-mail em formato inválido.")
        String email,

        @NotNull(message = "Data de nascimento é obrigatória.")
        @Past(message = "Data de nascimento deve ser uma data no passado.")
        LocalDate dataNascimento,

        @NotNull(message = "Renda mensal é obrigatória.")
        @DecimalMin(value = "0.00", message = "Renda mensal não pode ser negativa.")
        BigDecimal rendaMensal) {
}
