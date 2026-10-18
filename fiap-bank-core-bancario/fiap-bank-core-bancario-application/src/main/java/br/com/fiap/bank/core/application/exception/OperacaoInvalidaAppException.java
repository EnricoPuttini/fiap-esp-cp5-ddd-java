package br.com.fiap.bank.core.application.exception;

/** Espelha {@code domain.exception.OperacaoInvalidaException}, e também cobre a String de operação inválida vinda do DTO. */
public class OperacaoInvalidaAppException extends AppException {
    public OperacaoInvalidaAppException(String message) {
        super(message);
    }
}
