package com.deliverytech.delivery_api.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.request.ClienteRequestDTO;
import com.deliverytech.delivery_api.dto.response.ClienteResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.repository.ClienteRepository;
import com.deliverytech.delivery_api.service.ClienteService;

@Service
@Transactional
public class ClienteServiceImpl implements ClienteService{
    
    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ClienteResponseDTO cadastrar(ClienteRequestDTO dto) {

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado." + dto.getEmail());
        }
        Cliente cliente = modelMapper.map(dto, Cliente.class);
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());
        Cliente saveSalvo = clienteRepository.save(cliente);

        return modelMapper.map(saveSalvo, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO atualizar(Long id, ClienteRequestDTO dto) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com ID: " + id));

        if (clienteRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já cadastrado." + dto.getEmail());
        }
        if (dto.getNome() == null || dto.getNome().isEmpty()) {
            throw new BusinessException("Nome não pode ser vazio.");
        }
        if (dto.getEmail() == null || dto.getEmail().isEmpty()) {
            throw new BusinessException("Email não pode ser vazio.");
        }
        clienteExistente.setNome(dto.getNome());
        clienteExistente.setEmail(dto.getEmail());
        clienteExistente.setTelefone(dto.getTelefone());
        clienteExistente.setEndereco(dto.getEndereco());

        Cliente saveAtualizado = clienteRepository.save(clienteExistente);

        return modelMapper.map(saveAtualizado, ClienteResponseDTO.class);
    }

    @Override
    public ClienteResponseDTO ativarDesativarCliente(Long id) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new BusinessException("Cliente não encontrado com ID: " + id));
        clienteExistente.setAtivo(!clienteExistente.getAtivo());
        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }

    @Override
    public List<ClienteResponseDTO> listarAtivos() {

        List<Cliente> clientesAtivos = clienteRepository.findByAtivoTrue();

        return  clientesAtivos.stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<ClienteResponseDTO> buscarPorNome(String nome) {

        List<Cliente> clientes = clienteRepository.findByNomeContainingIgnoreCase(nome);

        return  clientes.stream()
                .map(c -> modelMapper.map(c, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ClienteResponseDTO buscarPorEmail(String email) {

        Cliente cliente = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com email: " + email));

        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

}
