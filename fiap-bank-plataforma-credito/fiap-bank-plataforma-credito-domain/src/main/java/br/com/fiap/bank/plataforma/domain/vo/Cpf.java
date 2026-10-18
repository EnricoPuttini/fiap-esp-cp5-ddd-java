package br.com.fiap.bank.plataforma.domain.vo;

import br.com.fiap.bank.plataforma.domain.exception.CpfInvalidoException;

/**
 * Value Object de CPF. Aceita o valor com ou sem máscara e guarda sempre
 * apenas os 11 dígitos. Valida somente o formato (os CPFs de demonstração do
 * ecossistema não passam no cálculo do dígito verificador).
 */
public record Cpf(String valor) {

    public Cpf {
        if (valor == null || !valor.matches("\\d{11}")) {
            throw new CpfInvalidoException("CPF inválido: deve conter 11 dígitos numéricos.");
        }
    }

    public static Cpf of(String bruto) {
        if (bruto == null) {
            throw new CpfInvalidoException("CPF é obrigatório.");
        }
        return new Cpf(bruto.replaceAll("\\D", ""));
    }
}
