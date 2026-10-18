package br.com.fiap.bank.plataforma.presentation.config;

import br.com.fiap.bank.plataforma.application.dto.SolicitarCreditoRequest;
import br.com.fiap.bank.plataforma.application.service.SolicitacaoCreditoService;
import java.math.BigDecimal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Carga inicial (mesmas 3 solicitações do legado) para a demonstração do ecossistema. */
@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final SolicitacaoCreditoService service;

    @Override
    public void run(String... args) {
        try {
            service.solicitar(new SolicitarCreditoRequest("12345678900", "Ana Silva",
                    "EMPRESTIMO_PESSOAL", new BigDecimal("15000.00"), 24, new BigDecimal("7500.00")));
            service.solicitar(new SolicitarCreditoRequest("98765432100", "Carlos Eduardo Oliveira",
                    "CREDITO_IMOBILIARIO", new BigDecimal("350000.00"), 180, new BigDecimal("12000.00")));
            service.solicitar(new SolicitarCreditoRequest("55544433399", "Mariana Souza",
                    "CREDITO_AUTOMOTIVO", new BigDecimal("45000.00"), 48, new BigDecimal("4200.00")));
            log.info("Plataforma de Credito: dados iniciais carregados com sucesso!");
        } catch (Exception e) {
            log.error("Erro ao carregar dados iniciais da Plataforma de Credito", e);
        }
    }
}
