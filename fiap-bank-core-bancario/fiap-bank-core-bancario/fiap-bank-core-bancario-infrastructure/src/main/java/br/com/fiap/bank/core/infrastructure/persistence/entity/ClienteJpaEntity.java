package br.com.fiap.bank.core.infrastructure.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Mapeamento relacional (Adapter de persistência) de {@code Cliente}.
 * <p>
 * Esta classe é propositalmente "burra" (sem comportamento de negócio) -
 * ela só existe para o Hibernate conseguir mapear tabela/colunas. Toda
 * regra de negócio vive na entidade de domínio {@code Cliente}, nunca aqui.
 */
@Entity
@Table(name = "tb_cliente", uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
public class ClienteJpaEntity {

    @Id
    private UUID id;

    @Column(nullable = false, length = 11)
    private String cpf;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false)
    private String email;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "renda_mensal", nullable = false, precision = 15, scale = 2)
    private BigDecimal rendaMensal;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    protected ClienteJpaEntity() {
        // exigido pelo JPA
    }

    public ClienteJpaEntity(UUID id, String cpf, String nome, String email, LocalDate dataNascimento,
            BigDecimal rendaMensal, LocalDateTime dataCadastro) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.rendaMensal = rendaMensal;
        this.dataCadastro = dataCadastro;
    }

    public UUID getId() {
        return id;
    }

    public String getCpf() {
        return cpf;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public BigDecimal getRendaMensal() {
        return rendaMensal;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRendaMensal(BigDecimal rendaMensal) {
        this.rendaMensal = rendaMensal;
    }
}
