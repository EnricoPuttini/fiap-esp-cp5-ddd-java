package br.com.fiap.bank.plataforma.application.service;

import br.com.fiap.bank.plataforma.application.dto.DecisaoRequest;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponse;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.exception.CpfInvalidoAppException;
import br.com.fiap.bank.plataforma.application.exception.DecisaoInvalidaAppException;
import br.com.fiap.bank.plataforma.application.exception.SolicitacaoNaoEncontradaAppException;
import br.com.fiap.bank.plataforma.application.mapper.SolicitacaoCreditoMapper;
import br.com.fiap.bank.plataforma.domain.exception.CpfInvalidoException;
import br.com.fiap.bank.plataforma.domain.exception.SolicitacaoJaDecididaException;
import br.com.fiap.bank.plataforma.domain.gateway.BureauGateway;
import br.com.fiap.bank.plataforma.domain.gateway.CrmGateway;
import br.com.fiap.bank.plataforma.domain.gateway.ScrGateway;
import br.com.fiap.bank.plataforma.domain.model.DecisaoCredito;
import br.com.fiap.bank.plataforma.domain.model.SolicitacaoCredito;
import br.com.fiap.bank.plataforma.domain.repository.SolicitacaoCreditoRepository;
import br.com.fiap.bank.plataforma.domain.service.MotorDeCredito;
import br.com.fiap.bank.plataforma.domain.vo.Cpf;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Casos de uso da solicitação de crédito. Orquestra repositório, gateways
 * (SCR, Bureau, CRM) e o motor de crédito; as regras em si ficam no domínio.
 */
@Service
@RequiredArgsConstructor
public class SolicitacaoCreditoService {

    private final SolicitacaoCreditoRepository repository;
    private final ScrGateway scrGateway;
    private final BureauGateway bureauGateway;
    private final CrmGateway crmGateway;
    private final MotorDeCredito motor = new MotorDeCredito();

    @Transactional
    public SolicitacaoCreditoResponse solicitar(SolicitarCreditoRequest request) {
        Cpf cpf = converterCpf(request.cpfCliente());

        boolean restricaoScr = scrGateway.possuiRestricoes(cpf);
        int scoreBureau = bureauGateway.consultarScore(cpf);

        DecisaoCredito decisao = motor.analisar(restricaoScr, scoreBureau,
                request.valorSolicitado(), request.quantidadeParcelas(), request.rendaMensal());

        SolicitacaoCredito nova = SolicitacaoCredito.abrir(cpf, request.nomeCliente(), request.tipoCredito(),
                request.valorSolicitado(), request.quantidadeParcelas(), request.rendaMensal(),
                restricaoScr, scoreBureau, decisao);
        SolicitacaoCredito salva = repository.salvar(nova);

        crmGateway.notificar(cpf, "Sua solicitacao de credito #" + salva.getId()
                + " esta no status: " + salva.getStatus());
        return SolicitacaoCreditoMapper.toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCreditoResponse> listar() {
        return repository.listarTodas().stream().map(SolicitacaoCreditoMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<SolicitacaoCreditoResponse> listarPorCpf(String cpfBruto) {
        return repository.listarPorCpf(converterCpf(cpfBruto)).stream()
                .map(SolicitacaoCreditoMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SolicitacaoCreditoResponse buscarPorId(Long id) {
        return SolicitacaoCreditoMapper.toResponse(buscarEntidade(id));
    }

    @Transactional
    public SolicitacaoCreditoResponse decidir(Long id, DecisaoRequest request) {
        SolicitacaoCredito solicitacao = buscarEntidade(id);
        try {
            if ("APROVADO".equalsIgnoreCase(request.decisao())) {
                solicitacao.aprovarPelaMesa(request.parecer());
            } else {
                solicitacao.reprovarPelaMesa(request.parecer());
            }
        } catch (SolicitacaoJaDecididaException e) {
            throw new DecisaoInvalidaAppException(e.getMessage());
        }
        SolicitacaoCredito salva = repository.salvar(solicitacao);

        String resultado = salva.getStatus().name().equals("APROVADO") ? "APROVADA" : "REPROVADA";
        crmGateway.notificar(salva.getCpfCliente(), "Sua solicitacao de credito #" + salva.getId()
                + " foi " + resultado + " pela Mesa de Operacoes.");
        return SolicitacaoCreditoMapper.toResponse(salva);
    }

    private SolicitacaoCredito buscarEntidade(Long id) {
        return repository.buscarPorId(id).orElseThrow(() ->
                new SolicitacaoNaoEncontradaAppException("Solicitacao de credito nao encontrada com ID: " + id));
    }

    private Cpf converterCpf(String bruto) {
        try {
            return Cpf.of(bruto);
        } catch (CpfInvalidoException e) {
            throw new CpfInvalidoAppException(e.getMessage());
        }
    }
}
