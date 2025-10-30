package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido cadastrar(Pedido pedido) {
        validarDadosPedido(pedido);
        
        // Gerar número do pedido
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8));
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");
        
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public Optional<Pedido> buscarPorId(Long id) {
        return pedidoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<Pedido> listarPedidosCliente(Long clienteId) {
        return pedidoRepository.findByClienteId(clienteId);
    }

    public Pedido atualizarStatus(Long id, String novoStatus) {
        Pedido pedido = buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido não encontrado: " + id));
        
        pedido.setStatus(novoStatus.toUpperCase());
        return pedidoRepository.save(pedido);
    }

    @Transactional(readOnly = true)
    public List<Pedido> buscarPorStatus(String status) {
        return pedidoRepository.findByStatus(status.toUpperCase());
    }

    private void validarDadosPedido(Pedido pedido) {
        if (pedido.getClienteId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório");
        }

        if (pedido.getRestauranteId() == null) {
            throw new IllegalArgumentException("Restaurante é obrigatório");
        }

        if (pedido.getValorTotal() == null || pedido.getValorTotal().signum() <= 0) {
            throw new IllegalArgumentException("Valor total deve ser maior que zero");
        }

        if (pedido.getItens() == null || pedido.getItens().trim().isEmpty()) {
            throw new IllegalArgumentException("Itens do pedido são obrigatórios");
        }
    }
}