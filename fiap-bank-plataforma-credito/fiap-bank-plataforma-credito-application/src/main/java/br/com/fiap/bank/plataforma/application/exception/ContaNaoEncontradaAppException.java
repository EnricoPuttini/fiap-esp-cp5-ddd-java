package br.com.fiap.bank.plataforma.application.exception;

/** Cliente sem conta no Core Bancário (404). Espelha a exception de domínio para que a presentation nunca importe o domain. */
public class ContaNaoEncontradaAppException extends RuntimeException {
    public ContaNaoEncontradaAppException(String message) {
        super(message);
    }
}
