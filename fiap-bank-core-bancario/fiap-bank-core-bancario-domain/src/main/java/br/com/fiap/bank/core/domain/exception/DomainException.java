package br.com.fiap.bank.core.domain.exception;

/** Superclasse de todas as exceptions de regra de negócio do domínio. */
public abstract class DomainException extends RuntimeException {
    protected DomainException(String message) {
        super(message);
    }
}
