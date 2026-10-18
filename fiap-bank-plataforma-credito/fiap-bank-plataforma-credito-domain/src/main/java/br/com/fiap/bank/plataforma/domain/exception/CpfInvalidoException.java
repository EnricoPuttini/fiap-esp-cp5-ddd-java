package br.com.fiap.bank.plataforma.domain.exception;

/** CPF fora do formato esperado. */
public class CpfInvalidoException extends DomainException {
    public CpfInvalidoException(String message) {
        super(message);
    }
}
