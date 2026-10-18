package br.com.fiap.bank.core.domain.exception;

/** Lançada ao tentar abrir uma conta duplicada para o mesmo cliente/número. */
public class ContaJaCadastradaException extends DomainException {
    public ContaJaCadastradaException(String message) {
        super(message);
    }
}
