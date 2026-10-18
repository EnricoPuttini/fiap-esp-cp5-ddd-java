package br.com.fiap.bank.core.presentation.controller;

import br.com.fiap.bank.core.application.dto.AtualizarSaldoRequest;
import br.com.fiap.bank.core.application.dto.ContaResponse;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.application.service.ContaService;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Recurso RESTful {@code /api/v1/contas}, conforme a matriz de endpoints
 * do anexo 10.1. A alteração de saldo é tratada como sub-recurso
 * ({@code /contas/{numeroConta}/saldo}) via PUT, suportando crédito e
 * débito atômicos.
 */
@RestController
@RequestMapping("/api/v1/contas")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @GetMapping
    public ResponseEntity<List<ContaResponse>> listar() {
        return ResponseEntity.ok(contaService.listar());
    }

    @GetMapping("/{numeroConta}")
    public ResponseEntity<ContaResponse> buscarPorNumero(@PathVariable String numeroConta) {
        return ResponseEntity.ok(contaService.buscarPorNumero(numeroConta));
    }

    @GetMapping("/cliente/{cpf}")
    public ResponseEntity<ContaResponse> buscarPorCpfCliente(@PathVariable String cpf) {
        return ResponseEntity.ok(contaService.buscarPorCpfCliente(cpf));
    }

    @PostMapping
    public ResponseEntity<ContaResponse> abrir(@Valid @RequestBody CriarContaRequest request) {
        ContaResponse criada = contaService.abrir(request);
        return ResponseEntity.created(URI.create("/api/v1/contas/" + criada.numeroConta())).body(criada);
    }

    @PutMapping("/{numeroConta}/saldo")
    public ResponseEntity<ContaResponse> atualizarSaldo(@PathVariable String numeroConta,
            @Valid @RequestBody AtualizarSaldoRequest request) {
        return ResponseEntity.ok(contaService.atualizarSaldo(numeroConta, request));
    }

    @DeleteMapping("/{numeroConta}")
    public ResponseEntity<Void> encerrar(@PathVariable String numeroConta) {
        contaService.encerrar(numeroConta);
        return ResponseEntity.noContent().build();
    }
}
