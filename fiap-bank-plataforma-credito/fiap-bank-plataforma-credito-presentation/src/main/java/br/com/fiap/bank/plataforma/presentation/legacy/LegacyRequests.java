package br.com.fiap.bank.plataforma.presentation.legacy;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/** Payloads das rotas legadas {@code /api/credito/**}, exatamente como os front-ends os enviam. */
public final class LegacyRequests {

    private LegacyRequests() {
    }

    public record ConsultarStatusRequest(@NotBlank(message = "cpfCliente é obrigatório.") String cpfCliente) {
    }

    public record BuscarSolicitacaoRequest(@NotNull(message = "solicitacaoId é obrigatório.") Long solicitacaoId) {
    }

    public record AprovarSolicitacaoRequest(@NotNull(message = "solicitacaoId é obrigatório.") Long solicitacaoId,
                                            String parecerOperador) {
    }

    public record ReprovarSolicitacaoRequest(@NotNull(message = "solicitacaoId é obrigatório.") Long solicitacaoId,
                                             String motivoReprovacao) {
    }
}
