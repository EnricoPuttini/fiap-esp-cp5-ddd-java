package br.com.fiap.bank.plataforma.application.exception;

/** Sistema externo indisponível (503). Espelha a exception de domínio para que a presentation nunca importe o domain. */
public class ServicoIndisponivelAppException extends RuntimeException {
    public ServicoIndisponivelAppException(String message) {
        super(message);
    }
}
