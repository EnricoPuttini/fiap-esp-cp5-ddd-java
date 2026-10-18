package br.com.fiap.bank.core.domain.exception;

/** Lançada para operações financeiras com instrução inválida (nem DEPOSITO nem SAQUE). */
public class OperacaoInvalidaException extends DomainException {
    public OperacaoInvalidaException(String message) {
        super(message);
    }
}
