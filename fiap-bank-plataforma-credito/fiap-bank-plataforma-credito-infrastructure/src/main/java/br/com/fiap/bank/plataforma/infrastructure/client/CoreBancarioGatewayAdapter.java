package br.com.fiap.bank.plataforma.infrastructure.client;

import br.com.fiap.bank.plataforma.domain.exception.ServicoExternoIndisponivelException;
import br.com.fiap.bank.plataforma.domain.gateway.CoreBancarioGateway;
import br.com.fiap.bank.plataforma.domain.model.ContaCore;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import feign.FeignException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter do Core Bancário. 404 vira {@code Optional.empty()}; qualquer outra
 * falha (Core offline, 5xx) vira {@link ServicoExternoIndisponivelException} -
 * em vez de devolver dados fictícios como fazia o código legado.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CoreBancarioGatewayAdapter implements CoreBancarioGateway {

    private final CoreBancarioFeignClient feignClient;

    @Override
    public Optional<ContaCore> consultarContaPorCpf(Cpf cpf) {
        try {
            var c = feignClient.consultarContaPorCpf(cpf.valor());
            return Optional.of(new ContaCore(c.numeroConta(), c.agencia(), c.saldo(), c.status()));
        } catch (FeignException.NotFound e) {
            return Optional.empty();
        } catch (Exception e) {
            log.warn("Core Bancário indisponível ao consultar o CPF {}: {}", cpf.valor(), e.getMessage());
            throw new ServicoExternoIndisponivelException("Core Bancário indisponível no momento.");
        }
    }
}
