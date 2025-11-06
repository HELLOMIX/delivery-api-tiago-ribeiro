package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import com.deliverytech.delivery_api.model.Restaurante;

import lombok.Data;

@Data
public class RestauranteResponseDTO {
    
    private Long id;

    private String nome;

    private String categoria;

    private String endereco;

    private String telefone;

    private BigDecimal taxaEntrega;

    private BigDecimal avaliacao;

    private Boolean ativo;

    public RestauranteResponseDTO(Restaurante restaurante) {
        this.id = restaurante.getId();
        this.nome = restaurante.getNome();
        this.categoria = restaurante.getCategoria();
        this.endereco = restaurante.getEndereco();
        this.telefone = restaurante.getTelefone();
        this.taxaEntrega = restaurante.getTaxaEntrega();
        this.avaliacao = restaurante.getAvaliacao();
        this.ativo = restaurante.getAtivo();
    }

}
