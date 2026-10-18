package br.com.fiap.bank.plataforma.presentation.controller;

import br.com.fiap.bank.plataforma.application.dto.DecisaoRequest;
import br.com.fiap.bank.plataforma.application.dto.SolicitacaoCreditoResponse;
import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.SolicitacaoCreditoService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API RESTful de solicitações de crédito. O recurso é o substantivo no plural
 * ({@code solicitacoes-credito}); a ação é dada pelo verbo HTTP.
 */
@RestController
@RequestMapping("/api/v1/solicitacoes-credito")
@RequiredArgsConstructor
public class SolicitacaoCreditoController {

    private final SolicitacaoCreditoService service;

    /** Lista todas as solicitações ou, com {@code ?cpf=}, só as do cliente. */
    @GetMapping
    public ResponseEntity<List<SolicitacaoCreditoResponse>> listar(@RequestParam(required = false) String cpf) {
        return ResponseEntity.ok(cpf == null || cpf.isBlank() ? service.listar() : service.listarPorCpf(cpf));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitacaoCreditoResponse> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<SolicitacaoCreditoResponse> solicitar(@Valid @RequestBody SolicitarCreditoRequest request) {
        SolicitacaoCreditoResponse criada = service.solicitar(request);
        return ResponseEntity.created(URI.create("/api/v1/solicitacoes-credito/" + criada.id())).body(criada);
    }

    /** Decisão da Mesa de Operações: sub-recurso {@code decisao} atualizado com PUT. */
    @PutMapping("/{id}/decisao")
    public ResponseEntity<SolicitacaoCreditoResponse> decidir(@PathVariable Long id,
                                                              @Valid @RequestBody DecisaoRequest request) {
        return ResponseEntity.ok(service.decidir(id, request));
    }
}
