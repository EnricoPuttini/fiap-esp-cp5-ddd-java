package br.com.fiap.bank.core.domain.vo;

import java.util.Objects;

/**
 * Value Object que representa um CPF.
 * <p>
 * Encapsula a limpeza (remoção de máscara) e a validação estrutural: exige
 * exatamente 11 dígitos numéricos, no formato limpo ou com máscara
 * canônica (XXX.XXX.XXX-XX), exatamente como especificado no anexo 10.3
 * do Checkpoint 5. Propositalmente não valida o dígito verificador pelo
 * algoritmo da Receita Federal: os CPFs de teste usados nos dados
 * pré-carregados do ecossistema (ex.: 12345678900) não passam nesse
 * checksum, e a especificação do checkpoint não exige essa validação.
 */
public final class Cpf {

    private final String digitos;

    private Cpf(String digitos) {
        this.digitos = digitos;
    }

    public static Cpf of(String valor) {
        if (valor == null) {
            throw new IllegalArgumentException("CPF não pode ser nulo.");
        }
        String limpo = valor.replaceAll("\\D", "");
        if (limpo.length() != 11) {
            throw new IllegalArgumentException("CPF deve conter exatamente 11 dígitos numéricos.");
        }
        return new Cpf(limpo);
    }

    /** Retorna o CPF só com dígitos (ex: "12345678900"). */
    public String valor() {
        return digitos;
    }

    /** Retorna o CPF formatado com máscara (ex: "123.456.789-00"). */
    public String formatado() {
        return digitos.substring(0, 3) + "." + digitos.substring(3, 6) + "." + digitos.substring(6, 9)
                + "-" + digitos.substring(9, 11);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cpf cpf)) return false;
        return digitos.equals(cpf.digitos);
    }

    @Override
    public int hashCode() {
        return Objects.hash(digitos);
    }

    @Override
    public String toString() {
        return digitos;
    }
}
