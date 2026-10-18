package br.com.fiap.bank.plataforma.domain.exception;

/** Raiz das exceptions de negócio do domínio de crédito. */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}
