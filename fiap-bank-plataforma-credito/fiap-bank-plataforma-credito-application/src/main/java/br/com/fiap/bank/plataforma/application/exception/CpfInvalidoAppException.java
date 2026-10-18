package br.com.fiap.bank.plataforma.application.exception;

/** CPF fora do formato esperado (400). Espelha a exception de domínio para que a presentation nunca importe o domain. */
public class CpfInvalidoAppException extends RuntimeException {
    public CpfInvalidoAppException(String message) {
        super(message);
    }
}
