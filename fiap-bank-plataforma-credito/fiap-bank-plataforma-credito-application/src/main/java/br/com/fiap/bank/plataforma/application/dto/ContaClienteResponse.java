package br.com.fiap.bank.plataforma.application.dto;

import java.math.BigDecimal;

/** Conta do cliente no Core Bancário, na visão da Plataforma de Crédito. */
public record ContaClienteResponse(String numeroConta, String agencia, BigDecimal saldo, String status) {
}
