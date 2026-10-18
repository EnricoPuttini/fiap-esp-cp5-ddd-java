package br.com.fiap.bank.plataforma.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Decisão da Mesa de Operações sobre uma solicitação em análise
 * ({@code PUT /api/v1/solicitacoes-credito/{id}/decisao}).
 * {@code parecer} é o parecer (aprovação) ou o motivo (reprovação).
 */
public record DecisaoRequest(

        @NotBlank(message = "Decisão é obrigatória.")
        @Pattern(regexp = "(?i)^(APROVADO|REPROVADO)$", message = "Decisão deve ser APROVADO ou REPROVADO.")
        String decisao,

        @Size(max = 400, message = "Parecer deve ter no máximo 400 caracteres.")
        String parecer) {
}
