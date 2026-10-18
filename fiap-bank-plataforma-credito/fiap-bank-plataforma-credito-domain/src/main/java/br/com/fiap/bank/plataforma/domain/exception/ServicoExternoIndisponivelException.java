package br.com.fiap.bank.plataforma.domain.exception;

/** Falha de comunicação com um sistema externo (Core Bancário, SCR, Bureau, CRM). */
public class ServicoExternoIndisponivelException extends DomainException {
    public ServicoExternoIndisponivelException(String message) {
        super(message);
    }
}
