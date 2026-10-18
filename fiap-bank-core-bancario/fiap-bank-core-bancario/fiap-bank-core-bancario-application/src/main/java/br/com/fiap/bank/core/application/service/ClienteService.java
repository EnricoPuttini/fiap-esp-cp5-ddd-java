package br.com.fiap.bank.core.application.service;

import br.com.fiap.bank.core.application.dto.AtualizarClienteRequest;
import br.com.fiap.bank.core.application.dto.ClienteResponse;
import br.com.fiap.bank.core.application.dto.CriarClienteRequest;
import br.com.fiap.bank.core.application.exception.ClienteNaoEncontradoAppException;
import br.com.fiap.bank.core.application.exception.CpfJaCadastradoAppException;
import br.com.fiap.bank.core.application.mapper.ClienteMapper;
import br.com.fiap.bank.core.domain.model.Cliente;
import br.com.fiap.bank.core.domain.repository.ClienteRepository;
import br.com.fiap.bank.core.domain.vo.Cpf;
import br.com.fiap.bank.core.domain.vo.Dinheiro;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Orquestra os casos de uso de gestão de clientes. Depende apenas da porta
 * de domínio {@link ClienteRepository} - nunca de um JpaRepository
 * diretamente, preservando a inversão de dependência do Ports &amp; Adapters.
 * <p>
 * Toda exception de domínio capturada aqui é traduzida para o equivalente
 * em {@code application.exception}, para que a camada de apresentação
 * nunca precise importar {@code domain.exception}.
 */
@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listar() {
        return clienteRepository.findAll().stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(UUID id) {
        return ClienteMapper.toResponse(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorCpf(String cpf) {
        Cliente cliente = clienteRepository.findByCpf(Cpf.of(cpf))
                .orElseThrow(() -> new ClienteNaoEncontradoAppException("Cliente não encontrado para o CPF: " + cpf));
        return ClienteMapper.toResponse(cliente);
    }

    @Transactional
    public ClienteResponse criar(CriarClienteRequest request) {
        Cpf cpf = Cpf.of(request.cpf());
        if (clienteRepository.existsByCpf(cpf)) {
            throw new CpfJaCadastradoAppException("Já existe um cliente cadastrado com o CPF: " + cpf.formatado());
        }

        Cliente cliente = Cliente.novo(
                cpf,
                request.nome(),
                request.email(),
                request.dataNascimento(),
                Dinheiro.ofNaoNegativo(request.rendaMensal()));

        return ClienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public ClienteResponse atualizar(UUID id, AtualizarClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);
        cliente.atualizarDados(
                request.nome(),
                request.email(),
                request.dataNascimento(),
                Dinheiro.ofNaoNegativo(request.rendaMensal()));
        return ClienteMapper.toResponse(clienteRepository.save(cliente));
    }

    @Transactional
    public void remover(UUID id) {
        buscarEntidadePorId(id); // garante 404 antes de tentar remover
        clienteRepository.deleteById(id);
    }

    private Cliente buscarEntidadePorId(UUID id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ClienteNaoEncontradoAppException("Cliente não encontrado para o id: " + id));
    }
}
