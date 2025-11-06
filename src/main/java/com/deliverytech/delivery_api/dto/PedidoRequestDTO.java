package com.deliverytech.delivery_api.dto;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PedidoRequestDTO {
    
    @NotNull(message = "O ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "O ID do restaurante é obrigatório")
    private Long restauranteId;

    private String observacoes;

    @NotEmpty(message = "Os itens do pedido são obrigatórios")
    private List<ItemPedidoDTO> itens;

}
