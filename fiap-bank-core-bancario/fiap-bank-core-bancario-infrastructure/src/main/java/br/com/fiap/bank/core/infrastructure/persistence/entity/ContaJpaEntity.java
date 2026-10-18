package br.com.fiap.bank.core.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mapeamento relacional (Adapter de persistência) de {@code Conta}.
 * Assim como {@code ClienteJpaEntity}, não carrega nenhuma regra de negócio.
 */
@Entity
@Table(name = "tb_conta", uniqueConstraints = @UniqueConstraint(columnNames = "numero_conta"))
public class ContaJpaEntity {

    @Id
    private UUID id;

    @Column(name = "numero_conta", nullable = false, length = 20)
    private String numeroConta;

    @Column(nullable = false, length = 4)
    private String agencia;

    @Column(name = "tipo_conta", nullable = false, length = 20)
    private String tipoConta;

    @Column(name = "cpf_cliente", nullable = false, length = 11)
    private String cpfCliente;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal saldo;

    @Column(nullable = false, length = 10)
    private String status;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura;

    protected ContaJpaEntity() {
        // exigido pelo JPA
    }

    public ContaJpaEntity(UUID id, String numeroConta, String agencia, String tipoConta, String cpfCliente,
            BigDecimal saldo, String status, LocalDateTime dataAbertura) {
        this.id = id;
        this.numeroConta = numeroConta;
        this.agencia = agencia;
        this.tipoConta = tipoConta;
        this.cpfCliente = cpfCliente;
        this.saldo = saldo;
        this.status = status;
        this.dataAbertura = dataAbertura;
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

    public String getTipoConta() {
        return tipoConta;
    }

    public String getCpfCliente() {
        return cpfCliente;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
