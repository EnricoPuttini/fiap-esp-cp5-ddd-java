package br.com.fiap.bank.plataforma.infrastructure.client;

import java.math.BigDecimal;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/** Client declarativo do Core Bancário (porta 8080). Consome a API RESTful nova do Core. */
@FeignClient(name = "coreBancarioClient", url = "${core.bancario.url:http://localhost:8080/api/v1}")
public interface CoreBancarioFeignClient {

    @GetMapping("/contas/cliente/{cpf}")
    ContaCorePayload consultarContaPorCpf(@PathVariable("cpf") String cpf);

    /** Subconjunto do JSON de conta devolvido pelo Core; campos extras são ignorados. */
    record ContaCorePayload(String numeroConta, String agencia, BigDecimal saldo, String status) {
    }
}
