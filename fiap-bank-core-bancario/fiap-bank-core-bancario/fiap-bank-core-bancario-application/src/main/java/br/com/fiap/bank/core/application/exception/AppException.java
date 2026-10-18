package br.com.fiap.bank.core.application.exception;

/**
 * Superclasse das exceptions de negócio expostas pela camada de aplicação.
 * Permite que a camada de apresentação trate erros de negócio sem nunca
 * precisar importar {@code br.com.fiap.bank.core.domain.exception}.
 */
public abstract class AppException extends RuntimeException {
    protected AppException(String message) {
        super(message);
    }
}
