package br.com.fiap.bank.plataforma.infrastructure.client;

import br.com.fiap.bank.plataforma.domain.gateway.ScrGateway;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import org.springframework.stereotype.Component;

/** Client do SCR (Banco Central). Simulação: CPFs terminados em 9 possuem restrição. */
@Component
public class ScrClientAdapter implements ScrGateway {

    @Override
    public boolean possuiRestricoes(Cpf cpf) {
        return cpf.valor().endsWith("9");
    }
}
