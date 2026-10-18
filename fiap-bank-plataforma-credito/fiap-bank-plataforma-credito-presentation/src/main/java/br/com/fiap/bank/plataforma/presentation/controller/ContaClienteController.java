package br.com.fiap.bank.plataforma.presentation.controller;

import br.com.fiap.bank.plataforma.application.dto.ContaClienteResponse;
import br.com.fiap.bank.plataforma.application.service.ContaClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Sub-recurso {@code conta} do cliente, obtido do Core Bancário. */
@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ContaClienteController {

    private final ContaClienteService service;

    @GetMapping("/{cpf}/conta")
    public ResponseEntity<ContaClienteResponse> consultarConta(@PathVariable String cpf) {
        return ResponseEntity.ok(service.consultarPorCpf(cpf));
    }
}
