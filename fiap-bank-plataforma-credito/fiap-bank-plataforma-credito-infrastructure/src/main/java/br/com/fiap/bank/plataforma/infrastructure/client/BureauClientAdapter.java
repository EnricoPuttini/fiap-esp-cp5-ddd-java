package br.com.fiap.bank.plataforma.infrastructure.client;

import br.com.fiap.bank.plataforma.domain.gateway.BureauGateway;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import org.springframework.stereotype.Component;

/** Client do Bureau de Crédito. Simulação: score determinístico entre 400 e 950 a partir do CPF. */
@Component
public class BureauClientAdapter implements BureauGateway {

    @Override
    public int consultarScore(Cpf cpf) {
        int soma = 0;
        for (char c : cpf.valor().toCharArray()) {
            soma += Character.getNumericValue(c);
        }
        return 400 + (soma * 9) % 551;
    }
}
