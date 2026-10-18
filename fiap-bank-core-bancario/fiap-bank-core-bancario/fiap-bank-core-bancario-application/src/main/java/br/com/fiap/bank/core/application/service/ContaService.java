package br.com.fiap.bank.core.application.service;

import br.com.fiap.bank.core.application.dto.AtualizarSaldoRequest;
import br.com.fiap.bank.core.application.dto.ContaResponse;
import br.com.fiap.bank.core.application.dto.CriarContaRequest;
import br.com.fiap.bank.core.application.exception.ClienteNaoEncontradoAppException;
import br.com.fiap.bank.core.application.exception.ContaInativaAppException;
import br.com.fiap.bank.core.application.exception.ContaJaCadastradaAppException;
import br.com.fiap.bank.core.application.exception.ContaNaoEncontradaAppException;
import br.com.fiap.bank.core.application.exception.OperacaoInvalidaAppException;
import br.com.fiap.bank.core.application.exception.SaldoInsuficienteAppException;
import br.com.fiap.bank.core.application.mapper.ContaMapper;
import br.com.fiap.bank.core.domain.enums.TipoConta;
import br.com.fiap.bank.core.domain.exception.ContaInativaException;
import br.com.fiap.bank.core.domain.exception.SaldoInsuficienteException;
import br.com.fiap.bank.core.domain.model.Conta;
import br.com.fiap.bank.core.domain.repository.ClienteRepository;
import br.com.fiap.bank.core.domain.repository.ContaRepository;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquestra os casos de uso de contas bancárias. Depende apenas das portas
 * de domínio {@link ContaRepository} e {@link ClienteRepository}.
 * <p>
 * As exceptions lançadas pelas regras de negócio dentro da entidade
 * {@link Conta} (que são do pacote {@code domain.exception}) são
 * capturadas aqui e traduzidas para o equivalente em
 * {@code application.exception}, para que a camada de apresentação nunca
 * precise importar {@code domain.exception} diretamente.
 */
@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final ClienteRepository clienteRepository;

    public ContaService(ContaRepository contaRepository, ClienteRepository clienteRepository) {
        this.contaRepository = contaRepository;
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ContaResponse> listar() {
        return contaRepository.findAll().stream()
                .map(ContaMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorNumero(String numeroConta) {
        return ContaMapper.toResponse(buscarEntidadePorNumero(numeroConta));
    }

    @Transactional(readOnly = true)
    public ContaResponse buscarPorCpfCliente(String cpf) {
        Conta conta = contaRepository.findByCpfCliente(Cpf.of(cpf))
                .orElseThrow(() -> new ContaNaoEncontradaAppException("Nenhuma conta encontrada para o CPF: " + cpf));
        return ContaMapper.toResponse(conta);
    }

    @Transactional
    public ContaResponse abrir(CriarContaRequest request) {
        Cpf cpfCliente = Cpf.of(request.cpfCliente());
        TipoConta tipoConta = converterTipoConta(request.tipoConta());

        if (!clienteRepository.existsByCpf(cpfCliente)) {
            throw new ClienteNaoEncontradoAppException(
                    "Não é possível abrir conta: nenhum cliente cadastrado com o CPF " + cpfCliente.formatado());
        }
        if (contaRepository.findByCpfCliente(cpfCliente).isPresent()) {
            throw new ContaJaCadastradaAppException(
                    "O cliente de CPF " + cpfCliente.formatado() + " já possui uma conta aberta.");
        }

        String numeroConta = gerarNumeroConta();
        while (contaRepository.existsByNumeroConta(numeroConta)) {
            numeroConta = gerarNumeroConta();
        }

        Conta conta = Conta.abrir(
                numeroConta,
                request.agenciaOuDefault(),
                tipoConta,
                cpfCliente,
                Dinheiro.ofNaoNegativo(request.saldoInicial()));

        return ContaMapper.toResponse(contaRepository.save(conta));
    }

    @Transactional
    public ContaResponse atualizarSaldo(String numeroConta, AtualizarSaldoRequest request) {
        Conta conta = buscarEntidadePorNumero(numeroConta);
        Dinheiro valor = Dinheiro.ofPositivo(request.valor());

        try {
            if (request.isDeposito()) {
                conta.depositar(valor);
            } else if (request.isSaque()) {
                conta.sacar(valor);
            } else {
                throw new OperacaoInvalidaAppException("Operação deve ser DEPOSITO ou SAQUE.");
            }
        } catch (SaldoInsuficienteException e) {
            throw new SaldoInsuficienteAppException(e.getMessage());
        } catch (ContaInativaException e) {
            throw new ContaInativaAppException(e.getMessage());
        }

        return ContaMapper.toResponse(contaRepository.save(conta));
    }

    @Transactional
    public void encerrar(String numeroConta) {
        Conta conta = buscarEntidadePorNumero(numeroConta);
        conta.encerrar();
        contaRepository.save(conta);
    }

    private Conta buscarEntidadePorNumero(String numeroConta) {
        return contaRepository.findByNumeroConta(numeroConta)
                .orElseThrow(() -> new ContaNaoEncontradaAppException("Conta não encontrada: " + numeroConta));
    }

    /** Converte a String do DTO (ex.: "corrente") no enum de domínio, validando o valor. */
    private TipoConta converterTipoConta(String valor) {
        try {
            return TipoConta.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new OperacaoInvalidaAppException(
                    "Tipo de conta inválido: " + valor + ". Valores aceitos: CORRENTE, POUPANCA, SALARIO.");
        }
    }

    /** Gera um número de conta no formato NNNNN-D (dígito verificador simples). */
    private String gerarNumeroConta() {
        int base = ThreadLocalRandom.current().nextInt(10000, 99999);
        int dv = base % 9;
        return base + "-" + dv;
    }
}
