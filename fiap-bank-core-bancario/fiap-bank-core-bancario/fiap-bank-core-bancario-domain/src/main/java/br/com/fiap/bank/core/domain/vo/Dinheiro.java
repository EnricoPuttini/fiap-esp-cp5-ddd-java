package br.com.fiap.bank.core.domain.vo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Value Object monetário imutável. Centraliza escala/arredondamento e
 * impede a criação de valores negativos onde isso não faz sentido de
 * negócio, evitando que BigDecimal "cru" circule pelo domínio sem regras.
 */
public final class Dinheiro {

    public static final Dinheiro ZERO = new Dinheiro(BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP));

    private final BigDecimal valor;

    private Dinheiro(BigDecimal valor) {
        this.valor = valor;
    }

    public static Dinheiro of(BigDecimal valor) {
        if (valor == null) {
            throw new IllegalArgumentException("Valor monetário não pode ser nulo.");
        }
        return new Dinheiro(valor.setScale(2, RoundingMode.HALF_UP));
    }

    public static Dinheiro ofNaoNegativo(BigDecimal valor) {
        Dinheiro dinheiro = of(valor);
        if (dinheiro.isNegativo()) {
            throw new IllegalArgumentException("Valor monetário não pode ser negativo.");
        }
        return dinheiro;
    }

    public static Dinheiro ofPositivo(BigDecimal valor) {
        Dinheiro dinheiro = of(valor);
        if (dinheiro.valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor monetário deve ser maior que zero.");
        }
        return dinheiro;
    }

    public Dinheiro somar(Dinheiro outro) {
        return new Dinheiro(this.valor.add(outro.valor));
    }

    public Dinheiro subtrair(Dinheiro outro) {
        return new Dinheiro(this.valor.subtract(outro.valor));
    }

    public boolean isMenorQue(Dinheiro outro) {
        return this.valor.compareTo(outro.valor) < 0;
    }

    public boolean isNegativo() {
        return this.valor.compareTo(BigDecimal.ZERO) < 0;
    }

    public BigDecimal valor() {
        return valor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Dinheiro dinheiro)) return false;
        return valor.compareTo(dinheiro.valor) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(valor.stripTrailingZeros());
    }

    @Override
    public String toString() {
        return valor.toPlainString();
    }
}
