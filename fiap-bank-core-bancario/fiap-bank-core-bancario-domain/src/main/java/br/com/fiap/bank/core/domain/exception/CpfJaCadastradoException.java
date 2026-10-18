package br.com.fiap.bank.core.domain.exception;

/** Lançada ao tentar cadastrar um cliente com um CPF que já existe na base. */
public class CpfJaCadastradoException extends DomainException {
    public CpfJaCadastradoException(String message) {
        super(message);
    }
}
