package com.deliverytech.delivery_api.repository;

import com.deliverytech.delivery_api.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Pedidos por cliente
    List<Pedido> findByClienteId(Long clienteId);

    // Pedidos por status
    List<Pedido> findByStatus(String status);

    // Buscar por data do pedido
    List<Pedido> findByDataPedido(LocalDateTime dataPedido);

    // Buscar por itens (conteúdo)
    List<Pedido> findByItensContainingIgnoreCase(String itens);

    // Buscar por número do pedido
    List<Pedido> findByNumeroPedido(String numeroPedido);
}