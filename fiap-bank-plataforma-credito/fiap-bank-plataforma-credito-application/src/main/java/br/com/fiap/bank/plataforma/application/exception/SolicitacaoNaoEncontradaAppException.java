package br.com.fiap.bank.plataforma.application.exception;

/** Solicitação de crédito inexistente (404). Espelha a exception de domínio para que a presentation nunca importe o domain. */
public class SolicitacaoNaoEncontradaAppException extends RuntimeException {
    public SolicitacaoNaoEncontradaAppException(String message) {
        super(message);
    }
}
