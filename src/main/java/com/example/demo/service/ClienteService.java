package com.example.demo.service;

import com.example.demo.dto.cliente.ClienteRequest;
import com.example.demo.dto.cliente.ClienteResponse;
import com.example.demo.dto.cliente.EnderecoDTO;
import com.example.demo.model.Cliente;
import com.example.demo.model.Endereco;
import com.example.demo.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional
    public ClienteResponse criarCliente(ClienteRequest request) {
        String cpfLimpo = limparCPF(request.getCpf());
        
        if (clienteRepository.existsByCpf(cpfLimpo)) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setCpf(cpfLimpo);
        cliente.setEndereco(toEndereco(request.getEndereco()));

        cliente = clienteRepository.save(cliente);
        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        String cpfLimpo = limparCPF(request.getCpf());
        
        if (clienteRepository.existsByCpfAndIdNot(cpfLimpo, id)) {
            throw new RuntimeException("CPF já cadastrado para outro cliente");
        }

        cliente.setNome(request.getNome());
        cliente.setCpf(cpfLimpo);
        cliente.setEndereco(toEndereco(request.getEndereco()));

        cliente = clienteRepository.save(cliente);
        return toResponse(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new RuntimeException("Cliente não encontrado");
        }
        clienteRepository.deleteById(id);
    }

    private String limparCPF(String cpf) {
        return cpf.replaceAll("[^0-9]", "");
    }

    private Endereco toEndereco(EnderecoDTO dto) {
        return new Endereco(
                dto.getLogradouro(),
                dto.getBairro(),
                dto.getCidade(),
                dto.getEstado(),
                dto.getCep()
        );
    }

    private EnderecoDTO toEnderecoDTO(Endereco endereco) {
        return new EnderecoDTO(
                endereco.getLogradouro(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }

    private ClienteResponse toResponse(Cliente cliente) {
        return ClienteResponse.builder()
                .id(cliente.getId())
                .nome(cliente.getNome())
                .cpf(formatarCPF(cliente.getCpf()))
                .endereco(toEnderecoDTO(cliente.getEndereco()))
                .createdAt(cliente.getCreatedAt())
                .updatedAt(cliente.getUpdatedAt())
                .build();
    }

    private String formatarCPF(String cpf) {
        // Formata CPF: 12345678901 -> 123.456.789-01
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}
