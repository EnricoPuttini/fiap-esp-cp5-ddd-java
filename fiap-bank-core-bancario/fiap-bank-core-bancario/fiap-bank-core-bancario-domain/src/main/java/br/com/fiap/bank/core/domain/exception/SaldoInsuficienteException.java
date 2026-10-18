package br.com.fiap.bank.core.domain.exception;

/** Lançada quando um saque excede o saldo disponível na conta. */
public class SaldoInsuficienteException extends DomainException {
    public SaldoInsuficienteException(String message) {
        super(message);
    }
}
