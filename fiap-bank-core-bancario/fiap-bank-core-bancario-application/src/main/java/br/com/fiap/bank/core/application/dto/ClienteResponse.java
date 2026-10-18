package br.com.fiap.bank.core.application.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/** Representação de saída de um cliente. Nunca expõe a entidade de domínio diretamente. */
public record ClienteResponse(
        UUID id,
        String cpf,
        String nome,
        String email,
        LocalDate dataNascimento,
        BigDecimal rendaMensal,
        LocalDateTime dataCadastro) {
}
