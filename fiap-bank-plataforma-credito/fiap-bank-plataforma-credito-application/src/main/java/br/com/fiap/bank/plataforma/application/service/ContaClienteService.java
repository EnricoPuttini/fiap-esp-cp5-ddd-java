package br.com.fiap.bank.plataforma.application.service;

import br.com.fiap.bank.plataforma.application.dto.ContaClienteResponse;
import br.com.fiap.bank.plataforma.application.exception.ContaNaoEncontradaAppException;
import br.com.fiap.bank.plataforma.application.exception.CpfInvalidoAppException;
import br.com.fiap.bank.plataforma.application.exception.ServicoIndisponivelAppException;
import br.com.fiap.bank.plataforma.application.mapper.SolicitacaoCreditoMapper;
import br.com.fiap.bank.plataforma.domain.exception.CpfInvalidoException;
import br.com.fiap.bank.plataforma.domain.exception.ServicoExternoIndisponivelException;
import br.com.fiap.bank.plataforma.domain.gateway.CoreBancarioGateway;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Consulta a conta do cliente no Core Bancário (via gateway). */
@Service
@RequiredArgsConstructor
public class ContaClienteService {

    private final CoreBancarioGateway coreBancarioGateway;

    public ContaClienteResponse consultarPorCpf(String cpfBruto) {
        try {
            Cpf cpf = Cpf.of(cpfBruto);
            return coreBancarioGateway.consultarContaPorCpf(cpf)
                    .map(SolicitacaoCreditoMapper::toResponse)
                    .orElseThrow(() -> new ContaNaoEncontradaAppException(
                            "Nenhuma conta encontrada no Core Bancario para o CPF informado."));
        } catch (CpfInvalidoException e) {
            throw new CpfInvalidoAppException(e.getMessage());
        } catch (ServicoExternoIndisponivelException e) {
            throw new ServicoIndisponivelAppException(e.getMessage());
        }
    }
}
