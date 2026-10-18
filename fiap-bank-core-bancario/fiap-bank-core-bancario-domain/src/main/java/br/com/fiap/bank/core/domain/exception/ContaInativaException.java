package br.com.fiap.bank.core.domain.exception;

/** Lançada ao tentar movimentar uma conta já encerrada/inativa. */
public class ContaInativaException extends DomainException {
    public ContaInativaException(String message) {
        super(message);
    }
}
