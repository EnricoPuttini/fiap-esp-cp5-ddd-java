package br.com.fiap.bank.plataforma.application.exception;

/** Solicitação não está em análise e não aceita decisão (409). Espelha a exception de domínio para que a presentation nunca importe o domain. */
public class DecisaoInvalidaAppException extends RuntimeException {
    public DecisaoInvalidaAppException(String message) {
        super(message);
    }
}
