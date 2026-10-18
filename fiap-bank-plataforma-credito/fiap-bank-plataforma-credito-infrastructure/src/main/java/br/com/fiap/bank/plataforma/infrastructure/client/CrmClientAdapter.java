package br.com.fiap.bank.plataforma.infrastructure.client;

import br.com.fiap.bank.plataforma.domain.gateway.CrmGateway;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** Client do CRM / notificações. Simulação: registra a mensagem no log. */
@Slf4j
@Component
public class CrmClientAdapter implements CrmGateway {

    @Override
    public void notificar(Cpf cpf, String mensagem) {
        log.info("[CRM] Notificacao enviada para CPF {}: {}", cpf.valor(), mensagem);
    }
}
