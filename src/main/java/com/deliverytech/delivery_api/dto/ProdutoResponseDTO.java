package com.deliverytech.delivery_api.dto;

import com.deliverytech.delivery_api.model.Produto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProdutoResponseDTO {
    
    private Long id;

    private String nome;

    private String descricao;

    private BigDecimal preco;

    private String categoria;

    private Boolean disponivel;

    private Long restauranteId;

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.descricao = produto.getDescricao();
        this.preco = produto.getPreco();
        this.categoria = produto.getCategoria();
        this.disponivel = produto.getDisponivel();
        this.restauranteId = produto.getRestauranteId();
    }

}
