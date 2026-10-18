package br.com.fiap.bank.plataforma.domain.exception;

/** Tentativa de decidir uma solicitação que não está em análise. */
public class SolicitacaoJaDecididaException extends DomainException {
    public SolicitacaoJaDecididaException(String message) {
        super(message);
    }
}
