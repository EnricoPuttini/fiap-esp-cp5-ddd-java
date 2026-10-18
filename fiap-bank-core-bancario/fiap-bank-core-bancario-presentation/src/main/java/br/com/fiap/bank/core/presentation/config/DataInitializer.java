package br.com.fiap.bank.core.presentation.config;

import lombok.RequiredArgsConstructor;
import br.com.fiap.bank.core.application.dto.CriarClienteRequest;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.application.service.ClienteService;
import br.com.fiap.bank.core.application.service.ContaService;
import java.math.BigDecimal;
import java.time.LocalDate;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Carga inicial de dados, usando exatamente os clientes/contas descritos
 * no README raiz do monorepo, para que os dois front-ends legados
 * (Mesa de Operações e Internet Banking Mobile) encontrem os mesmos
 * CPFs de demonstração que já usam hoje.
 */
@RequiredArgsConstructor
@Component
public class DataInitializer {

    private final ClienteService clienteService;
    private final ContaService contaService;

    @Override
    public void run(String... args) {
        try {
            clienteService.criar(new CriarClienteRequest(
                    "12345678900", "Ana Silva", "ana.silva@fiapbank.com",
                    LocalDate.of(1990, 4, 12), new BigDecimal("7500.00")));
            contaService.abrir(new CriarContaRequest(
                    "12345678900", "0001", "CORRENTE", new BigDecimal("12500.00")));

            clienteService.criar(new CriarClienteRequest(
                    "98765432100", "Carlos Eduardo Oliveira", "carlos.oliveira@fiapbank.com",
                    LocalDate.of(1985, 9, 23), new BigDecimal("12000.00")));
            contaService.abrir(new CriarContaRequest(
                    "98765432100", "0001", "CORRENTE", new BigDecimal("45800.50")));

            System.out.println(">>> Core Bancário: dados iniciais carregados com sucesso!");
        } catch (Exception e) {
            System.err.println(">>> Erro ao carregar dados iniciais do Core Bancário: " + e.getMessage());
        }
    }
}
