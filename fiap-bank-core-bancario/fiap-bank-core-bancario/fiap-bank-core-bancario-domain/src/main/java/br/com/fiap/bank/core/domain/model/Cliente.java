package br.com.fiap.bank.core.domain.model;

import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

/**
 * Entidade de domínio que representa um correntista do FIAP Bank.
 * <p>
 * Totalmente livre de anotações de framework (sem {@code @Entity},
 * sem Bean Validation) - regra de isolamento de domínio puro.
 */
public class Cliente {

    private final UUID id;
    private final Cpf cpf;
    private String nome;
    private String email;
    private final LocalDate dataNascimento;
    private Dinheiro rendaMensal;
    private final LocalDateTime dataCadastro;

    private Cliente(UUID id, Cpf cpf, String nome, String email, LocalDate dataNascimento,
            Dinheiro rendaMensal, LocalDateTime dataCadastro) {
        this.id = id;
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.dataNascimento = dataNascimento;
        this.rendaMensal = rendaMensal;
        this.dataCadastro = dataCadastro;
    }

    /** Cria um novo cliente (cadastro inicial). */
    public static Cliente novo(Cpf cpf, String nome, String email, LocalDate dataNascimento, Dinheiro rendaMensal) {
        validarNome(nome);
        validarEmail(email);
        validarDataNascimento(dataNascimento);
        return new Cliente(UUID.randomUUID(), cpf, nome, email, dataNascimento, rendaMensal, LocalDateTime.now());
    }

    /** Reconstitui um cliente já existente (usado pela infraestrutura ao ler do banco). */
    public static Cliente reconstruir(UUID id, Cpf cpf, String nome, String email, LocalDate dataNascimento,
            Dinheiro rendaMensal, LocalDateTime dataCadastro) {
        return new Cliente(id, cpf, nome, email, dataNascimento, rendaMensal, dataCadastro);
    }

    /** Atualiza os dados cadastrais do cliente (CPF e id nunca mudam). */
    public void atualizarDados(String nome, String email, LocalDate dataNascimento, Dinheiro rendaMensal) {
        validarNome(nome);
        validarEmail(email);
        validarDataNascimento(dataNascimento);
        this.nome = nome;
        this.email = email;
        this.rendaMensal = rendaMensal;
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3 || nome.trim().length() > 150) {
            throw new IllegalArgumentException("Nome do titular deve ter entre 3 e 150 caracteres.");
        }
    }

    private static void validarEmail(String email) {
        if (email == null || !email.matches("^[\\w.+-]+@[\\w-]+\\.[a-zA-Z]{2,}$")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
    }

    private static void validarDataNascimento(LocalDate dataNascimento) {
        if (dataNascimento == null || !dataNascimento.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Data de nascimento deve ser uma data passada.");
        }
    }

    public UUID getId() {
        return id;
    }

    public Cpf getCpf() {
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

    public Dinheiro getRendaMensal() {
        return rendaMensal;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente cliente)) return false;
        return id.equals(cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
