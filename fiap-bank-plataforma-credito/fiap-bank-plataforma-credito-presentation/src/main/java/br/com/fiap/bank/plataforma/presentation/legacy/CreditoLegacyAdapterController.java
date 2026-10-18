package br.com.fiap.bank.plataforma.presentation.legacy;

import br.com.fiap.bank.plataforma.application.dto.ContaClienteResponse;
import br.com.fiap.bank.plataforma.application.dto.DecisaoRequest;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponse;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.ContaClienteService;
import br.com.fiap.bank.plataforma.application.service.SolicitacaoCreditoService;
import br.com.fiap.bank.plataforma.presentation.legacy.LegacyRequests.AprovarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.presentation.legacy.LegacyRequests.BuscarSolicitacaoRequest;
import br.com.fiap.bank.plataforma.presentation.legacy.LegacyRequests.ConsultarStatusRequest;
import br.com.fiap.bank.plataforma.presentation.legacy.LegacyRequests.ReprovarSolicitacaoRequest;
import jakarta.validation.Valid;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * ADAPTER de retrocompatibilidade. Mantém as 7 rotas "POST de ação" que o Angular
 * (4200) e a Mesa de Operações Thymeleaf (8082) já consomem, traduzindo cada uma
 * para os mesmos serviços de aplicação usados pela API RESTful nova
 * ({@code /api/v1/...}). Não contém regra de negócio: só adapta contrato.
 * Pode ser removido quando os front-ends migrarem para a API nova.
 */
@RestController
@RequestMapping("/api/credito")
@RequiredArgsConstructor
public class CreditoLegacyAdapterController {

    private final SolicitacaoCreditoService solicitacaoService;
    private final ContaClienteService contaClienteService;

    @PostMapping("/solicitarCredito")
    public ResponseEntity<SolicitacaoCreditoResponse> solicitarCredito(
            @Valid @RequestBody SolicitarCreditoRequest request) {
        return ResponseEntity.ok(solicitacaoService.solicitar(request));
    }

    @PostMapping("/consultarStatusCredito")
    public ResponseEntity<List<SolicitacaoCreditoResponse>> consultarStatusCredito(
            @Valid @RequestBody ConsultarStatusRequest request) {
        return ResponseEntity.ok(solicitacaoService.listarPorCpf(request.cpfCliente()));
    }

    @PostMapping("/buscarSolicitacaoPorId")
    public ResponseEntity<SolicitacaoCreditoResponse> buscarSolicitacaoPorId(
            @Valid @RequestBody BuscarSolicitacaoRequest request) {
        return ResponseEntity.ok(solicitacaoService.buscarPorId(request.solicitacaoId()));
    }

    @PostMapping("/listarTodasSolicitacoes")
    public ResponseEntity<List<SolicitacaoCreditoResponse>> listarTodasSolicitacoes() {
        return ResponseEntity.ok(solicitacaoService.listar());
    }

    @PostMapping("/aprovarSolicitacao")
    public ResponseEntity<SolicitacaoCreditoResponse> aprovarSolicitacao(
            @Valid @RequestBody AprovarSolicitacaoRequest request) {
        return ResponseEntity.ok(solicitacaoService.decidir(request.solicitacaoId(),
                new DecisaoRequest("APROVADO", request.parecerOperador())));
    }

    @PostMapping("/reprovarSolicitacao")
    public ResponseEntity<SolicitacaoCreditoResponse> reprovarSolicitacao(
            @Valid @RequestBody ReprovarSolicitacaoRequest request) {
        return ResponseEntity.ok(solicitacaoService.decidir(request.solicitacaoId(),
                new DecisaoRequest("REPROVADO", request.motivoReprovacao())));
    }

    /** O front-end espera {@code statusConta}; a API nova do Core chama o campo de {@code status}. */
    @PostMapping("/consultarClienteCore")
    public ResponseEntity<Map<String, Object>> consultarClienteCore(
            @Valid @RequestBody ConsultarStatusRequest request) {
        ContaClienteResponse conta = contaClienteService.consultarPorCpf(request.cpfCliente());
        Map<String, Object> legado = new LinkedHashMap<>();
        legado.put("statusConta", conta.status());
        legado.put("saldo", conta.saldo());
        legado.put("agencia", conta.agencia());
        legado.put("numeroConta", conta.numeroConta());
        return ResponseEntity.ok(legado);
    }
}
