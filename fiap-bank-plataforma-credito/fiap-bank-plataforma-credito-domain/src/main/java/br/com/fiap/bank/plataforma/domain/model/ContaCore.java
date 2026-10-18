package br.com.fiap.bank.plataforma.domain.model;

import java.math.BigDecimal;

/** Visão mínima da conta do cliente no Core Bancário, usada pela Plataforma de Crédito. */
public record ContaCore(String numeroConta, String agencia, BigDecimal saldo, String status) {
}
