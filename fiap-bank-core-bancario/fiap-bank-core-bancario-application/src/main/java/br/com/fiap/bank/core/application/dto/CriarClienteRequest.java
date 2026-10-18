package br.com.fiap.bank.core.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Contrato de entrada para {@code POST /api/v1/clientes}.
 * Validações conforme especificação do anexo 10.3 do Checkpoint 5.
 */
public record CriarClienteRequest(

        @NotBlank(message = "CPF é obrigatório.")
        @Pattern(
                regexp = "^(\\d{11}|\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2})$",
                message = "CPF deve conter 11 dígitos numéricos ou o formato XXX.XXX.XXX-XX.")
        String cpf,

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
