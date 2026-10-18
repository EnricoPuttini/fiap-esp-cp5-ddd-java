package br.com.fiap.bank.plataforma.domain.model;

/** Resultado do motor de crédito: status inicial da solicitação e o parecer automático. */
public record DecisaoCredito(StatusSolicitacao status, String parecer) {
}
