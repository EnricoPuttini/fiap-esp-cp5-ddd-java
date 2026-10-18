package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.enums.StatusConta;
import br.com.fiap.bank.core.domain.enums.TipoConta;
import br.com.fiap.bank.core.domain.exception.ContaInativaException;
import br.com.fiap.bank.core.domain.exception.SaldoInsuficienteException;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa uma conta bancária.
 * <p>
 * As regras de negócio de movimentação financeira (depositar/sacar) vivem
 * aqui, dentro da própria entidade - evitando o antipadrão de modelo
 * anêmico em que um serviço externo manipula o estado via setters.
 */
public class Conta {

    private final UUID id;
    private final String numeroConta;
    private final String agencia;
    private final TipoConta tipoConta;
    private final Cpf cpfCliente;
    private Dinheiro saldo;
    private StatusConta status;
    private final LocalDateTime dataAbertura;

    private Conta(UUID id, String numeroConta, String agencia, TipoConta tipoConta, Cpf cpfCliente,
            Dinheiro saldo, StatusConta status, LocalDateTime dataAbertura) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.tipoConta = tipoConta;
        this.cpfCliente = cpfCliente;
        this.saldo = saldo;
        this.status = status;
        this.dataAbertura = dataAbertura;
    }

    /** Abre uma nova conta bancária para um cliente já existente. */
    public static Conta abrir(String numeroConta, String agencia, TipoConta tipoConta, Cpf cpfCliente,
            Dinheiro saldoInicial) {
        if (numeroConta == null || numeroConta.isBlank()) {
            throw new IllegalArgumentException("Número da conta é obrigatório.");
        }
        if (tipoConta == null) {
            throw new IllegalArgumentException("Tipo de conta é obrigatório.");
        }
        return new Conta(UUID.randomUUID(), numeroConta, agencia, tipoConta, cpfCliente, saldoInicial,
                StatusConta.ATIVA, LocalDateTime.now());
    }

    /** Reconstitui uma conta já existente (usado pela infraestrutura ao ler do banco). */
    public static Conta reconstruir(UUID id, String numeroConta, String agencia, TipoConta tipoConta,
            Cpf cpfCliente, Dinheiro saldo, StatusConta status, LocalDateTime dataAbertura) {
        return new Conta(id, numeroConta, agencia, tipoConta, cpfCliente, saldo, status, dataAbertura);
    }

    /** Credita um valor na conta (operação DEPOSITO). */
    public void depositar(Dinheiro valor) {
        garantirContaAtiva();
        this.saldo = this.saldo.somar(valor);
    }

    /** Debita um valor da conta (operação SAQUE), validando saldo suficiente. */
    public void sacar(Dinheiro valor) {
        garantirContaAtiva();
        if (saldo.isMenorQue(valor)) {
            throw new SaldoInsuficienteException(
                    "Saldo insuficiente. Saldo atual: " + saldo + ", valor solicitado: " + valor);
        }
        this.saldo = this.saldo.subtrair(valor);
    }

    /** Encerra/inativa a conta de forma lógica (idempotente). */
    public void encerrar() {
        this.status = StatusConta.INATIVA;
    }

    private void garantirContaAtiva() {
        if (status == StatusConta.INATIVA) {
            throw new ContaInativaException("A conta " + numeroConta + " está inativa e não pode ser movimentada.");
        }
    }

    public UUID getId() {
        return id;
    }

    public String getNumeroConta() {
        return numeroConta;
    }

    public String getAgencia() {
        return agencia;
    }

    public TipoConta getTipoConta() {
        return tipoConta;
    }

    public Cpf getCpfCliente() {
        return cpfCliente;
    }

    public Dinheiro getSaldo() {
        return saldo;
    }

    public StatusConta getStatus() {
        return status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Conta conta)) return false;
        return id.equals(conta.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
