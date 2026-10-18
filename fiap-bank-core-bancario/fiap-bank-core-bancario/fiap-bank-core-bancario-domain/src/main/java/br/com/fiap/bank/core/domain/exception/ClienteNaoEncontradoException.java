package br.com.fiap.bank.core.domain.exception;

/** Lançada quando um cliente não é encontrado pelo identificador ou CPF informado. */
public class ClienteNaoEncontradoException extends DomainException {
    public ClienteNaoEncontradoException(String message) {
        super(message);
    }
}
