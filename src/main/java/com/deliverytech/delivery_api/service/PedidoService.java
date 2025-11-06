package com.deliverytech.delivery_api.service;

import com.deliverytech.delivery_api.dto.ItemPedidoDTO;
import com.deliverytech.delivery_api.dto.PedidoRequestDTO;
import com.deliverytech.delivery_api.dto.PedidoResponseDTO;
import com.deliverytech.delivery_api.model.Pedido;
import com.deliverytech.delivery_api.model.Produto;
import com.deliverytech.delivery_api.repository.PedidoRepository;
import com.deliverytech.delivery_api.repository.ProdutoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public PedidoResponseDTO cadastrar(PedidoRequestDTO dto) {

        // extrai ids e quantidades
        List<Long> produtoIds = dto.getItens().stream()
                .map(ItemPedidoDTO::getProdutoId)
                .collect(Collectors.toList());

        // busca produtos
        List<Produto> produtos = produtoRepository.findAllById(produtoIds);

        // criar mapa id -> quantidade
        Map<Long, Integer> quantidadePorProduto = dto.getItens().stream()
                .collect(Collectors.toMap(ItemPedidoDTO::getProdutoId, ItemPedidoDTO::getQuantidade, Integer::sum));

        // valida disponibilidade e calcula total considerando quantidade
        BigDecimal valorTotal = BigDecimal.ZERO;
        for (Produto produto : produtos) {
            if (produto.getDisponivel() == null || !produto.getDisponivel()) {
                throw new IllegalArgumentException("Produto não disponível: " + produto.getNome());
            }
            Integer qtd = quantidadePorProduto.getOrDefault(produto.getId(), 1);
            valorTotal = valorTotal.add(produto.getPreco().multiply(BigDecimal.valueOf(qtd)));
        }

        Pedido pedido = new Pedido();
        pedido.setClienteId(dto.getClienteId());
        pedido.setRestauranteId(dto.getRestauranteId());
        pedido.setObservacoes(dto.getObservacoes());
        // Gerar número do pedido
        pedido.setNumeroPedido(UUID.randomUUID().toString().substring(0, 8));
        pedido.setDataPedido(LocalDateTime.now());
        pedido.setStatus("PENDENTE");

        pedido.setValorTotal(valorTotal);

        // Definir itens do pedido como uma string "Nome xQtd, Nome2 xQtd2"
        String itensStr = produtos.stream()
                .map(p -> p.getNome() + " x" + quantidadePorProduto.getOrDefault(p.getId(), 1))
                .collect(Collectors.joining(", "));
        pedido.setItens(itensStr);

        return new PedidoResponseDTO(pedidoRepository.save(pedido));
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

    //private void validarDadosPedido(Pedido pedido) {
    //    if (pedido.getClienteId() == null) {
    //        throw new IllegalArgumentException("Cliente é obrigatório");
    //    }
//
    //    if (pedido.getRestauranteId() == null) {
    //        throw new IllegalArgumentException("Restaurante é obrigatório");
    //    }
//
    //    if (pedido.getValorTotal() == null || pedido.getValorTotal().signum() <= 0) {
    //        throw new IllegalArgumentException("Valor total deve ser maior que zero");
    //    }
//
    //    if (pedido.getItens() == null || pedido.getItens().trim().isEmpty()) {
    //        throw new IllegalArgumentException("Itens do pedido são obrigatórios");
    //    }
    //}
}