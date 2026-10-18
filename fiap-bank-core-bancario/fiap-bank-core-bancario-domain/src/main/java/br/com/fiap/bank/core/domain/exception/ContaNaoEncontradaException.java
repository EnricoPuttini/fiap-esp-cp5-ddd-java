package br.com.fiap.bank.core.domain.exception;

/** Lançada quando uma conta não é encontrada pelo identificador ou número informado. */
public class ContaNaoEncontradaException extends DomainException {
    public ContaNaoEncontradaException(String message) {
        super(message);
    }
}
