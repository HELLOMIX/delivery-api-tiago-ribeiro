package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "pedidos")
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_pedido", length = 20, nullable = false)
    private String numeroPedido;

    @Column(name = "data_pedido")
    private LocalDateTime dataPedido;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;

    @Column(name = "observacoes", length = 200)
    private String observacoes;

    @Column(name = "cliente_id")
    private Long clienteId;

    @Column(name = "restaurante_id")
    private Long restauranteId;

    @Column(name = "itens", length = 200)
    private String itens;
}